/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2019 Ruturaj Nene, Vishal Patel, Neha Gundecha, Kasi Karuppiah Alagappan
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package edu.northeastern.ccs.im.client;

import edu.northeastern.ccs.im.client.message.Message;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that continuously scans for incoming messages. Because of the CPU
 * overhead that this entails, we place this work off onto a separate worker
 * thread. This has the side effect of not interfering with the event dispatch
 * thread. Like all <code>SwingWorker</code>s each instance should be executed
 * exactly once.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public final class ScanForMessagesWorker extends SwingWorker<Void, Message> {
    /**
     * IM connection instance for which we are working.
     */
    private final IMConnection imConnection;

    /**
     * List holding all messages we have read and not yet pushed out to the
     * user.
     */
    private List<Message> messages;

    /**
     * Instance which actually performs the low-level I/O with the server.
     */
    private SocketNB realConnection;

    /**
     * Create an initialize the worker thread we will use to scan for incoming
     * messages.
     *
     * @param cimConnection Instance to which this will be attached.
     * @param sock          Socket instance which really hosts the connection to the
     *                      server
     */
    ScanForMessagesWorker(IMConnection cimConnection, SocketNB sock) {
        // Record the instance and connection we will be using
        imConnection = cimConnection;
        realConnection = sock;
        // Create the queue that will hold the messages received from over the
        // network
        messages = new CopyOnWriteArrayList<>();
    }

    /**
     * Executes the steps needed to switch which method is being executed.
     *
     * @return Null value needed to comply with original method definition
     */
    @Override
    protected Void doInBackground() {
        while (!isCancelled()) {
            realConnection.enqueueMessages(messages);
            if (!messages.isEmpty()) {
                // Add this message into our queue
                publish(messages.remove(0));
            }
        }
        return null;
    }

    @Override
    protected void process(List<Message> mess) {
        List<Message> publishList = new LinkedList<>();
        boolean flagForClosure = false;

        for (Message m : mess) {
            if (m.shouldPublish()) {
                publishList.add(m);
            }

            if (m.isQuit()) {
                flagForClosure = true;
            }
            // TODO: Check if the code is needed
            //else if (m.isNoAcknowledge()) {
            //     cancel(false);
            //     realConnection = null;
            //     imConnection.fireStatusChange(imConnection.getUserName());
            //} else if (m.isAcknowledge()) {
            //     imConnection.fireStatusChange(imConnection.getUserName());
            //}
        }

        if (!publishList.isEmpty()) {
            imConnection.fireSendMessages(publishList);
        }

        if (flagForClosure) {
            cancel(false);
            realConnection = null;
            imConnection.loggedOut();
            imConnection.fireStatusChange(imConnection.getUserName());
        }
    }
}