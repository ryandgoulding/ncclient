/*
 * Copyright © 2017 Velox Software and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.veloxsw.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NcclientProvider {

    private static final Logger LOG = LoggerFactory.getLogger(NcclientProvider.class);

    private final DataBroker dataBroker;

    public NcclientProvider(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("NcclientProvider Session Initiated");
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        LOG.info("NcclientProvider Closed");
    }
}