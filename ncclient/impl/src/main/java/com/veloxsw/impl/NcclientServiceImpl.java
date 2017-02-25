/*
 * Copyright © 2017 Velox Software and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.veloxsw.impl;

import com.google.common.util.concurrent.Futures;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ncclient.rev170225.*;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Future;

public class NcclientServiceImpl implements NcclientService {

    private static final Logger LOG = LoggerFactory.getLogger(NcclientServiceImpl.class);

    static final class UnixCommandReturnDTO {
        private final String output;
        private final String error;
        private final int returnCode;

        private UnixCommandReturnDTO(final String output, final String error, final int returnCode) {
            this.output = output;
            this.error = error;
            this.returnCode = returnCode;
        }

        static UnixCommandReturnDTO create(final String output, final String error, final int returnCode) {
            return new UnixCommandReturnDTO(output, error, returnCode);
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        public int getReturnCode() {
            return returnCode;
        }
    }

    static final class StreamGobbler implements Runnable {
        final InputStream inputStream;
        String output;
        private StreamGobbler(final InputStream inputStream) {
           this.inputStream = inputStream;
        }

        static StreamGobbler create(final InputStream inputStream) {
            return new StreamGobbler(inputStream);
        }


        @Override
        public void run() {
            final StringBuilder output = new StringBuilder();
            final Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                output.append(scanner.next());
            }
            this.output = output.toString();
            LOG.error(this.output);
            scanner.close();
            try {
                inputStream.close();
            } catch (final IOException e) {
                LOG.error("Fatal error reading stream", e);
            }
        }

        String getOutput() {
            return output;
        }
    }

    private static UnixCommandReturnDTO execCommand(final String command) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(command);

        final StreamGobbler stdout = StreamGobbler.create(process.getInputStream());
        final StreamGobbler stderr = StreamGobbler.create(process.getErrorStream());
        final Thread outThread = new Thread(stdout);
        final Thread errThread = new Thread(stderr);

        outThread.start();
        errThread.start();

        process.waitFor();

        outThread.join();
        errThread.join();

        return UnixCommandReturnDTO.create(stdout.getOutput(), stderr.getOutput(), process.exitValue());
    }

    @Override
    public Future<RpcResult<PythonVersionOutput>> pythonVersion() {
        try {
            final UnixCommandReturnDTO ret = execCommand("python --version");
            final PythonVersionOutputBuilder builder = new PythonVersionOutputBuilder();
            builder.setOutput(ret.getOutput());
            builder.setError(ret.getError());
            builder.setReturnCode(ret.getReturnCode());
            return RpcResultBuilder.success(builder).buildFuture();
        } catch (final IOException | InterruptedException e) {
            return Futures.immediateFailedFuture(e);
        }
    }

    @Override
    public Future<RpcResult<WhichPythonOutput>> whichPython() {
        try {
            final UnixCommandReturnDTO ret = execCommand("which python");
            final WhichPythonOutputBuilder builder = new WhichPythonOutputBuilder();
            builder.setOutput(ret.getOutput());
            builder.setError(ret.getError());
            builder.setReturnCode(ret.getReturnCode());
            return RpcResultBuilder.success(builder).buildFuture();
        } catch (final IOException | InterruptedException e) {
            return Futures.immediateFailedFuture(e);
        }
    }
}
