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

import edu.northeastern.ccs.im.client.exceptions.IllegalNameException;
import edu.northeastern.ccs.im.client.exceptions.IllegalOperationException;
import edu.northeastern.ccs.im.client.exceptions.InvalidListenerException;
import edu.northeastern.ccs.im.client.message.Message;
import edu.northeastern.ccs.im.client.message.MessageFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class manages the connection between an the IM client and the IM server.
 * Instances of this class can be relied upon to manage all the details of this
 * connection and sends alerts when appropriate. Instances of this class must be
 * constructed and connected before it can be used to transmit messages.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class IMConnection {

    /**
     * Real Connection instance which this class wraps and makes presentable to the
     * user
     */
    private SocketNB socketConnection;

    /**
     * List of instances that have registered as a listener for connection events.
     */
    private ArrayList<LinkListener> linkListeners;

    /**
     * List of instances that have registered as a listener for received message
     * events.
     */
    private ArrayList<MessageListener> messageListeners;

    /**
     * Server to which this connection will be made.
     */
    private String hostName;

    /**
     * Port to which this connection will be made.
     */
    private int portNum;

    /**
     * Name of the user for which this connection was formed.
     */
    private String userName;

    /**
     * Password of the user for which this connection was formed.
     */
    private String password;

    /**
     * Instance used to read the messages.
     */
    private MessageScanner messageScanner;

    /**
     * Creates an instance that will manage a connection with an IM server, but does
     * not begin the process of making a connection to the IM server.
     *
     * @param host The name of the host that this connection is using
     * @param port The port number to use.
     */
    public IMConnection(String host, int port) {
        linkListeners = new ArrayList<>();
        messageListeners = new ArrayList<>();
        hostName = host;
        portNum = port;
    }

    /**
     * Add the given listener to be notified whenever 1 or more Messages are
     * received from IM server via this connection.
     *
     * @param listener Instance which will begin to receive notifications of any
     *                 messages received by this IMConnection.
     * @throws InvalidListenerException Exception thrown when this is called with a
     *                                  value of null for {@code listener}
     */
    public void addMessageListener(MessageListener listener) {
        if (listener == null) {
            throw new InvalidListenerException("Cannot add (null) as a listener!");
        }
        messageListeners.add(listener);
    }

    /**
     * Send a message to log in to the IM server using the given username. For the
     * moment, you will automatically be logged in to the server, even if there is
     * already someone with that username.<br/>
     * Precondition: connectionActive() == false
     *
     * @return True if the connection was successfully made; false otherwise.
     * @throws IllegalNameException Exception thrown if we try to connect with an
     *                              illegal username. Legal usernames can only
     *                              contain letters and numbers.
     */
    public boolean connect() {
        try {
            socketConnection = new SocketNB(hostName, portNum);
            socketConnection.startIMConnection();
        } catch (IOException e) {
            // Report the error
            System.err.println("ERROR:  Could not make a connection to: " + hostName + " at port " + portNum);
            System.err.println("\tIf the settings look correct and your machine is connected to the Internet, report this error to Dr. Jump");
            // And print out the problem
            e.printStackTrace();

            // Return that the connection could not be made.
            return false;
        }

        // Holds the SwingWorker which is used to read and process all incoming data.
        // Create the background thread that handles our incoming messages.
        SwingWorker<Void, Message> workerBee = new ScanForMessagesWorker(this, socketConnection);
        // Start the worker bee scanning for messages.
        workerBee.execute();

        MessageScanner rms = MessageScanner.getInstance();
        addMessageListener(rms);
        messageScanner = rms;

        // Return that we were successful
        return true;
    }

    /**
     * Returns whether the instance is managing an active, logged-in connection
     * between the client and an IM server.
     *
     * @return True if the client is logged in to the server using this connection;
     * false otherwise.
     */
    public boolean connectionActive() {
        if (socketConnection == null) {
            return false;
        } else {
            return socketConnection.isConnected();
        }
    }

    /**
     * Break this connection with the IM server. Once this method is called, this
     * instance will need to be logged back in to the IM server to be usable.<br/>
     * Precondition: connectionActive() == true
     */
    public void disconnect() {
        Message quitMessage = MessageFactory.makeQuitMessage();
        sendMessage(quitMessage);
        KeyboardScanner.close();
    }

    /**
     * Gets an object which can be used to read what the user types in on the
     * keyboard without waiting. The object returned by this method should be used
     * rather than {@link Scanner} since {@code Scanner} will cause a program to
     * halt if there is no input.
     *
     * @return Instance of {@link KeyboardScanner} that can be used to read keyboard
     * input for this connection of the server.
     */
    public KeyboardScanner getKeyboardScanner() {
        return KeyboardScanner.getInstance();
    }

    /**
     * Gets an object which can be used to get the message sent by the server over
     * this connection. This is the only object that can be used to retrieve all
     * these messages.
     *
     * @return Instance of {@link MessageScanner} that can be used to read message
     * sent over this connection for this user.
     */
    public MessageScanner getMessageScanner() {
        if (messageScanner == null) {
            throw new IllegalOperationException("Cannot get a MessageScanner if you have not connected to the server!");
        }
        return messageScanner;
    }

    /**
     * Get the name of the user for which we have created this connection.
     *
     * @return Current value of the user name and/or the username with which we
     * logged in to this IM server.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Unless this is a &quot;special&quot; server message, this sends the given
     * message to all of the users logged in to the IM server. <br/>
     * Precondition: connectionActive() == true
     *
     * @param message Text of the message which will be broadcast to all users.
     */
    public void sendMessage(Message message) {
        if (!connectionActive()) {
            throw new IllegalOperationException("Cannot send a message if you are not connected to a server!\n");
        }

        socketConnection.print(message);
    }

    /**
     * Send a message to log in to the IM server using the given username. For the
     * moment, you will automatically be logged in to the server, even if there is
     * already someone with that username.<br/>
     * Precondition: connectionActive() == false
     *
     * @return True if the connection was successfully made; false otherwise.
     */
    public void login(String userName, String password) {
        this.userName = userName;
        this.password = password;

        // Now log in using this name.
        Message loginMessage = MessageFactory.makeHelloMessage(userName, password);

        // Send the message to log us into the system.
        sendMessage(loginMessage);
    }

    public void register(String userName, String password) {
        this.userName = userName;
        this.password = password;

        // Now log in using this name.
        Message registerMessage = MessageFactory.makeRegisterMessage(userName, password);

        // Send the message to log us into the system.
        sendMessage(registerMessage);
    }

    @SuppressWarnings({"unchecked"})
    protected void fireSendMessages(List<Message> mess) {
        ArrayList<MessageListener> targets;
        synchronized (this) {
            targets = (ArrayList<MessageListener>) messageListeners.clone();
        }
        for (MessageListener iml : targets) {
            iml.messagesReceived(mess.iterator());
        }
    }

    @SuppressWarnings("unchecked")
    protected void fireStatusChange(String userName) {
        ArrayList<LinkListener> targets;
        synchronized (this) {
            targets = (ArrayList<LinkListener>) linkListeners.clone();
        }
        for (LinkListener iml : targets) {
            iml.linkStatusUpdate(userName, this);
        }
    }

    protected void loggedOut() {
        socketConnection = null;
    }
}