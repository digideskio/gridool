/*
 * @(#)$Id$
 *
 * Copyright 2010-2011 Makoto YUI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Makoto YUI - initial implementation
 */
package gridool.sqlet.catalog;

import gridool.GridNode;
import gridool.dfs.GridXferService;
import gridool.sqlet.SqletException;
import gridool.sqlet.SqletException.SqletErrorType;
import gridool.util.GridUtils;
import gridool.util.csv.HeaderAwareCsvReader;
import gridool.util.io.FastBufferedInputStream;
import gridool.util.io.FileUtils;
import gridool.util.lang.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

/**
 * @author Makoto YUI
 */
public class MapReduceConf implements Serializable {
    private static final long serialVersionUID = -951607371007537258L;

    private final List<Reducer> reducers;

    public MapReduceConf() {
        this.reducers = new ArrayList<MapReduceConf.Reducer>();
    }

    public List<Reducer> getReducers() {
        return reducers;
    }

    public List<Reducer> getReducers(Comparator<Reducer> comparator) {
        Collections.sort(reducers, comparator);
        return reducers;
    }

    public void loadReducers(String uri) throws SqletException {
        if(uri.endsWith(".csv") || uri.endsWith(".CSV")) {
            final InputStream is;
            try {
                FileSystemManager fsManager = VFS.getManager();
                FileObject fileObj = fsManager.resolveFile(uri);
                FileContent fileContent = fileObj.getContent();
                is = fileContent.getInputStream();
            } catch (FileSystemException e) {
                throw new SqletException(SqletErrorType.configFailed, "failed to load a file: "
                        + uri, e);
            }
            InputStreamReader reader = new InputStreamReader(new FastBufferedInputStream(is));
            HeaderAwareCsvReader csvReader = new HeaderAwareCsvReader(reader, ',', '"');

            final Map<String, Integer> headerMap;
            try {
                headerMap = csvReader.parseHeader();
            } catch (IOException e) {
                throw new SqletException(SqletErrorType.configFailed, "failed to parse a header: "
                        + uri, e);
            }

            final int[] fieldIndexes = toFieldIndexes(headerMap);
            while(csvReader.next()) {
                String id = csvReader.get(fieldIndexes[0]);
                String nodeStr = csvReader.get(fieldIndexes[1]);
                String dbUrl = csvReader.get(fieldIndexes[2]);
                String user = csvReader.get(fieldIndexes[3]);
                String password = csvReader.get(fieldIndexes[4]);
                String xferPortStr = csvReader.get(fieldIndexes[5]);
                String shuffleDataSinkStr = csvReader.get(fieldIndexes[6]);
                String[] shuffleDataSink = FileUtils.parsePathExpressions(shuffleDataSinkStr, ",");
                
                Preconditions.checkNotNull(id, nodeStr);

                GridNode hostNode = GridUtils.getNode(nodeStr);
                int xferPort = (xferPortStr == null || xferPortStr.isEmpty()) ? GridXferService.DEFAULT_RECV_PORT
                        : Integer.parseInt(xferPortStr);
                Reducer r = new Reducer(id, hostNode, dbUrl, user, password, xferPort, shuffleDataSink);
                reducers.add(r);
            }
        } else {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }    

    private static int[] toFieldIndexes(@Nullable Map<String, Integer> map) {
        if(map == null) {
            return new int[] { 0, 1, 2, 3, 4, 5, 6 };
        } else {
            Integer c0 = map.get("ID");
            Integer c1 = map.get("NODE");
            Integer c2 = map.get("DBURL");
            Integer c3 = map.get("USER");
            Integer c4 = map.get("PASSWORD");
            Integer c5 = map.get("XFER_PORT");
            Integer c6 = map.get("SHUFFLE_DATASINK");

            Preconditions.checkNotNull(c0, c1, c2, c3, c4, c5, c6);

            final int[] indexes = new int[7];
            indexes[0] = c0.intValue();
            indexes[1] = c1.intValue();
            indexes[2] = c2.intValue();
            indexes[3] = c3.intValue();
            indexes[4] = c4.intValue();
            indexes[5] = c5.intValue();
            indexes[6] = c6.intValue();
            return indexes;
        }
    }

    public static final class Reducer implements Serializable {
        private static final long serialVersionUID = 7657523061627358443L;

        @Nonnull
        final String id;
        @Nonnull
        final GridNode host;
        @Nullable
        final String dbUrl;
        @Nullable
        final String user;
        @Nullable
        final String password;
        final int xferPort;
        @Nullable
        final String[] shuffleDataSink;

        public Reducer(@Nonnull String id, @Nonnull GridNode host, @Nullable String dbUrl, @Nullable String user, @Nullable String password, int xferPort, @Nullable String[] shuffleDataSink) {
            this.id = id;
            this.host = host;
            this.dbUrl = dbUrl;
            this.user = user;
            this.password = password;
            this.xferPort = xferPort;
            this.shuffleDataSink = shuffleDataSink;
        }

        public String getId() {
            return id;
        }

        public GridNode getHost() {
            return host;
        }

        public String getDbUrl() {
            return dbUrl;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public int getXferPort() {
            return xferPort;
        }

        public String[] getShuffleDataSink() {
            return shuffleDataSink;
        }

        @Override
        public String toString() {
            return "Reducer [id=" + id + ", host=" + host + ", dbUrl=" + dbUrl + ", user=" + user
                    + ", password=" + password + ", xferPort=" + xferPort + ", shuffleDataSink="
                    + Arrays.toString(shuffleDataSink) + "]";
        }               

    }

    @Override
    public String toString() {
        return reducers.toString();
    }

}
