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

import java.util.EventListener;
import java.util.Iterator;

/**
 * The listener interface for receiving events whenever message(s) are sent by
 * an IM server to this user. Classes interested in receiving these events
 * should implement this interface and register with an IMConnection instance's
 * addMessageListener method.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public interface MessageListener extends EventListener {
    /**
     * Invoked in registered listeners whenever one or more messages are
     * received from an IM server. The Iterator returned by this message will go
     * over all of the messages received from the IM server since the last time
     * this event occurred. {@code it} may refer to a different instance of
     * {@code Iterator} each time this event is raised.
     *
     * @param it {@code Iterator} that will retrieve the 1 or more
     *           {@link MessageImpl}s received since the last time the listener
     *           was notified. This {@code Iterator} cannot be used to retrieve
     *           messages that arrive after the original call to this method.
     */
    void messagesReceived(Iterator<Message> it);
}
