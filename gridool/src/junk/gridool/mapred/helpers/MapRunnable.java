/*
 * @(#)$Id$
 *
 * Copyright 2006-2008 Makoto YUI
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
package gridool.mapred.helpers;

import gridool.mapred.MapReduceException;
import gridool.mapred.OutputCollector;
import gridool.mapred.io.RecordReader;
import gridool.util.WritableComparable;
import xbird.util.io.Writable;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405@gmail.com)
 */
public interface MapRunnable<K1 extends WritableComparable, V1 extends Writable, K2 extends WritableComparable, V2 extends Writable> {

    void run(RecordReader<K1, V1> input, OutputCollector<K2, V2> output) throws MapReduceException;

}
