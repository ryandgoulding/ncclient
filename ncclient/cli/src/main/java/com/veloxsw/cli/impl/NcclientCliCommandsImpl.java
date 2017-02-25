/*
 * Copyright © 2017 Velox Software and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.veloxsw.cli.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.veloxsw.cli.api.NcclientCliCommands;

public class NcclientCliCommandsImpl implements NcclientCliCommands {

    private static final Logger LOG = LoggerFactory.getLogger(NcclientCliCommandsImpl.class);
    private final DataBroker dataBroker;

    public NcclientCliCommandsImpl(final DataBroker db) {
        this.dataBroker = db;
        LOG.info("NcclientCliCommandImpl initialized");
    }

    @Override
    public Object testCommand(Object testArgument) {
        return "This is a test implementation of test-command";
    }
}