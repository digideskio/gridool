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
package gridool.dht.ops;

import gridool.GridNode;

import javax.annotation.Nonnull;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405@gmail.com)
 */
public final class DestinatedAddOperation extends AddOperation {

    @Nonnull
    private transient final GridNode[] destinations;

    public DestinatedAddOperation(String idxName, @Nonnull GridNode... destinations) {
        super(idxName);
        this.destinations = destinations;
    }

    @Override
    public final int getMaxNumReplicas() {
        return 0;
    }

    @Nonnull
    public GridNode[] getDestinations() {
        return destinations;
    }

}
