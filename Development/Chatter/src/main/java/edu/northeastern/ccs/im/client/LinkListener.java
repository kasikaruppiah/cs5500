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

import java.util.EventListener;

/**
 * The listener interface for receiving notifications of when a user is logged
 * into or logged out of the IM server. Classes interested in receiving these
 * updates should implement this interface and register using an IMConnection
 * instance's addLinkListener method.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public interface LinkListener extends EventListener {
    /**
     * Invoked in registered listeners whenever the given user's status at the
     * server accessed though the given IMConnection instance is changed. This
     * method is called AFTER {@code connection} has been updated.
     *
     * @param userName   Name of the user whose status just changed.
     * @param connection Link to the IM server on which the users status has changed.
     */
    void linkStatusUpdate(String userName, IMConnection connection);

    /**
     * Invoked in registered listeners whenever any other person logs into or
     * out of a server. This method can be used to know when people are
     * connected or disconnected from the server.
     *
     * @param mujer     Buddy whose status just changed
     * @param connected True if the user has just logged in to the IM server; false if
     *                  the Buddy just logged out.
     */
    void linkBuddyUpdate(Buddy mujer, boolean connected);
}
