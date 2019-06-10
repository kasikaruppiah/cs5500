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

package edu.northeastern.ccs.im.client.message;

/**
 * Broadcast Message
 */
public class BroadcastMessage extends Message {
    /**
     * Create a broadcast message with the given text
     *
     * @param text text of the message
     */
    public BroadcastMessage(String text) {
        super(MessageType.BROADCAST, null, text);
    }

    /**
     * Create a broadcast message with the given type, sender and text
     *
     * @param type   handle for the message
     * @param sender senders identification
     * @param text   text of the message
     */
    public BroadcastMessage(MessageType type, String sender, String text) {
        super(type, sender, text);
    }

    /**
     * Check if the message should be published for processing
     *
     * @return boolean indicating if message should be processed
     */
    @Override
    public Boolean shouldPublish() {
        return true;
    }
}
