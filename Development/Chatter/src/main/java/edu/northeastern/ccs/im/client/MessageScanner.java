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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is based upon the design of the java.util.Scanner class, but
 * handles instances of {@link Message} received over an {@link IMConnection}.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class MessageScanner implements MessageListener, Iterator<Message> {

    /**
     * Singleton.
     */
    private static MessageScanner instance;
    /**
     * Worklist to which this thread adds any input strings.
     */
    private Queue<Message> messages;


    /**
     * Create a new instance of this class that will maintain a queue of
     * {@link Message}s to be processed by the client.
     */
    private MessageScanner() {
        messages = new ConcurrentLinkedQueue<>();
    }

    /**
     * Factory method for creating instances that will handle instances of Message
     * received over an IMConnection.
     */
    public static MessageScanner getInstance() {
        if (instance == null) {
            instance = new MessageScanner();
        }
        return instance;
    }

    /**
     * Returns true if there is another {@link Message} received from the IM
     * server to return.
     *
     * @return True if and only if this instance of the class has another
     * {@code Message} available. Unlike an {@link Iterator}, it is
     * possible for this method to return true after it has returned
     * false (e.g., once another {@code Message} is received).
     */
    public boolean hasNext() {
        return !messages.isEmpty();
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.kings.im.MessageListener#messagesReceived(java.util.Iterator)
     */
    @Override
    public void messagesReceived(Iterator<Message> it) {
        while (it.hasNext()) {
            Message mess = it.next();
            messages.add(mess);
        }
    }

    /**
     * Get the next Message instance that was received from the IMConection.
     *
     * @return Next message that was received the from the IM server.
     */
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There was no next Message to be returned!");
        }
        return messages.remove();
    }

    /**
     * This method is not implemented (it will always throw an
     * {@link UnsupportedOperationException}), but is required because it is
     * defined by the Iterator interface.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove a Message from IMScanner.");
    }
}
