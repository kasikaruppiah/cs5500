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

import java.lang.Thread.State;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is similar to the java.util.Scanner class, but is tuned to read
 * Strings from the keyboard. This class's methods will return immediately and,
 * if there is no input available, will not hang until the user types something
 * in (it is &quot;non-blocking&quot; in technical parlance).
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public final class KeyboardScanner {
    /**
     * To make this class "non-blocking", we open the scanner instance in
     * another thread. As a result, any blocking will occur in the other class.
     */
    private static Thread producer;

    /**
     * Worklist to which this thread adds any input strings.
     */
    private static List<String> messages;

    /**
     * Singleton instance which does all the work
     */
    private static KeyboardScanner singleton;

    /**
     * Creates a new instance of this class. Since, by definition, this class
     * takes in input from the keyboard we do not need any parameters for this
     * constructor.
     */
    private KeyboardScanner() {
    }

    /**
     * Stop the instance from looking for keyboard input. This method
     * <b>must</b> be called for the program to terminate.
     */
    @SuppressWarnings("deprecation")
    protected static void close() {
        if (producer != null) {
            producer.interrupt();
            producer.stop();
        }
    }

    /**
     * Returns the singleton instance of this class. If the singleton instance
     * does not yet exist, this method will instantiate it.
     *
     * @return Singleton instance that can be used to read keyboard input in a
     * non-blocking manner.
     */
    protected static KeyboardScanner getInstance() {
        // This will be a pseudo-singleton class to make life easier.
        if (singleton == null) {
            synchronized (KeyboardScanner.class) {
                if (singleton == null) {
                    singleton = new KeyboardScanner();
                    // Create the new queue of messages in which we will store
                    // each line that we input
                    messages = new CopyOnWriteArrayList<>();
                    // Create the class which produces the keyboard output this
                    // class will examine.
                    producer = new Thread(new Runnable() {
                        public void run() {
                            @SuppressWarnings("resource")
                            Scanner scan = new Scanner(System.in);
                            boolean done = false;
                            while (!done) {
                                String keyboardIn = scan.nextLine();
                                messages.add(keyboardIn);
                            }
                        }
                    });
                    producer.setName("Key scanning thread");
                    // Start the thread that reads in from the keyboard and
                    // blocks for the team.
                    producer.start();
                }
            }
        }
        return singleton;
    }

    /**
     * Restart scanning for keyboard input after we have closed some keyboard
     * scanner.
     */
    protected static void restart() {
        if ((producer != null) && (producer.getState() == State.TERMINATED)) {
            synchronized (KeyboardScanner.class) {
                if ((producer != null) && (producer.getState() == State.TERMINATED)) {
                    producer = new Thread(new Runnable() {
                        public void run() {
                            @SuppressWarnings("resource")
                            Scanner scan = new Scanner(System.in);
                            boolean done = false;
                            while (!done) {
                                String keyboardIn = scan.nextLine();
                                messages.add(keyboardIn);
                            }
                        }
                    });
                    // Start the thread that reads in from the keyboard and
                    // blocks for the team.
                    producer.start();
                }
            }
        }
    }

    /**
     * Returns true if there is another line of keyboard input. This method will
     * NOT block while waiting for input. This class does not advance past any
     * input.
     *
     * @return True if and only if this instance of the class has another line
     * of input from the keyboard available.
     * @see java.util.Scanner#hasNext()
     */
    public boolean hasNext() {
        return !messages.isEmpty();
    }

    /**
     * Returns the next word that the user typed at the keyboard.
     *
     * @return Next word of text typed by the user.
     * @throws NoSuchElementException Exception thrown if the user has not entered any new lines of
     *                                text ( {@link #next()} returns false).
     */
    public String next() {
        if (messages.isEmpty()) {
            throw new NoSuchElementException("No new text has been typed in!");
        }
        String line = messages.get(0);
        Scanner lineScanner = new Scanner(line);
        String word = lineScanner.next();
        int loc = line.indexOf(word);
        String remainder = line.substring(loc + word.length()).trim();
        if (remainder.length() == 0) {
            messages.remove(0);
        } else {
            messages.set(0, remainder);
        }
        lineScanner.close();
        return word;
    }

    /**
     * Returns the next line of text the user typed at the keyboard.
     *
     * @return Next line of text that was typed by the user.
     * @throws NoSuchElementException Exception thrown if the user has not entered any new lines of
     *                                text ( {@link #next()} returns false).
     */
    public String nextLine() {
        if (messages.isEmpty()) {
            throw new NoSuchElementException("No new text has been typed in!");
        }
        return messages.remove(0);
    }

    /**
     * Empty the messages
     */
    public void empty() {
        if (!messages.isEmpty()) {
            messages.clear();
        }
    }
}