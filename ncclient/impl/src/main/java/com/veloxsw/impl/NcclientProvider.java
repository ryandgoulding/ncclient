/*
 * Copyright © 2017 Velox Software and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.veloxsw.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ncclient.rev170225.NcclientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NcclientProvider {

    private static final Logger LOG = LoggerFactory.getLogger(NcclientProvider.class);

    private final DataBroker dataBroker;
    private final RpcProviderRegistry rpcProviderRegistry;
    private BindingAwareBroker.RpcRegistration<NcclientService> rpcRegistration;

    public NcclientProvider(final DataBroker dataBroker, final RpcProviderRegistry rpcProviderRegistry) {
        this.dataBroker = dataBroker;
        this.rpcProviderRegistry = rpcProviderRegistry;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        rpcRegistration = rpcProviderRegistry.addRpcImplementation(NcclientService.class,
                new NcclientServiceImpl());
        LOG.info("NcclientProvider Session Initiated");
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        rpcRegistration.close();
        LOG.info("NcclientProvider Closed");
    }
}