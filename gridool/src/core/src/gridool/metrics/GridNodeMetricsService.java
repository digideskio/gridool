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
package gridool.metrics;

import gridool.GridConfiguration;
import gridool.GridException;
import gridool.GridResourceRegistry;
import gridool.GridService;

import javax.annotation.Nonnull;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405@gmail.com)
 */
public final class GridNodeMetricsService implements GridService {

    private final GridNodeMetricsProvider provider;

    public GridNodeMetricsService(@Nonnull GridResourceRegistry registry, @Nonnull GridConfiguration config) {
        this.provider = new GridNodeMetricsProvider(registry, config);
        registry.setNodeMetricsService(this);
    }

    public String getServiceName() {
        return GridNodeMetricsService.class.getName();
    }

    public boolean isDaemon() {
        return true;
    }

    public GridNodeMetricsProvider getMetricsProvider() {
        return provider;
    }

    public void start() throws GridException {
        provider.start();
    }

    public void stop() throws GridException {
        provider.stop();
    }
}
