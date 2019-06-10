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

package edu.northeastern.ccs.im.client.command;

import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.client.message.Message;
import edu.northeastern.ccs.im.client.message.MessageFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupMessageCommand implements CommandInterface {
    private String receiverId;
    private String text;

    public GroupMessageCommand(String[] tokens) {
        receiverId = tokens[0].substring(1);
        ArrayList<String> test = new ArrayList<>(Arrays.asList(tokens));
        test.remove(0);
        text = String.join(" ", test);
    }

    /**
     * This method is the caller that performs the action.
     *
     * @param connection
     */
    @Override
    public void executeCommand(IMConnection connection) {
        Message message = MessageFactory.makeGroupMessage(receiverId, text);

        connection.sendMessage(message);
    }
}
