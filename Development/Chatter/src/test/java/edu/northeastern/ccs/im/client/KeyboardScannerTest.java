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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KeyboardScannerTest {
    private Field producerField;
    private Thread producer;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        producerField = KeyboardScanner.class.getDeclaredField("producer");
        producerField.setAccessible(true);

        producer = (Thread) producerField.get(null);
    }

    @AfterEach
    void tearDown() throws IllegalAccessException {
        producerField.set(null, null);
    }

    @Test
    void close() throws IllegalAccessException, InterruptedException {
        KeyboardScanner.getInstance();

        producer = (Thread) producerField.get(null);

        assertEquals(Thread.State.RUNNABLE, producer.getState());

        KeyboardScanner.close();

        Thread.sleep(1000);

        assertEquals(Thread.State.TERMINATED, producer.getState());
    }

    @Test
    void closeNull() throws IllegalAccessException {
        assertNull(producer);

        KeyboardScanner.close();

        producer = (Thread) producerField.get(null);

        assertNull(producer);
    }

    @Test
    void restartNull() throws IllegalAccessException {
        assertNull(producer);

        KeyboardScanner.restart();

        producer = (Thread) producerField.get(null);

        assertNull(producer);
    }

//    @Test
//    void restartTerminated() throws IllegalAccessException, InterruptedException {
//        KeyboardScanner.getInstance();
//
//        producer = (Thread) producerField.get(null);
//
//        assertNotNull(producer);
//
//        KeyboardScanner.close();
//
//        Thread.sleep(1000);
//
//        assertEquals(Thread.State.TERMINATED, producer.getState());
//
//        KeyboardScanner.restart();
//
//        producer = (Thread) producerField.get(null);
//
//        assertEquals(Thread.State.RUNNABLE, producer.getState());
//    }

    @Test
    void hasNext() {
    }

    @Test
    void next() {
    }

    @Test
    void nextLine() {
    }
}