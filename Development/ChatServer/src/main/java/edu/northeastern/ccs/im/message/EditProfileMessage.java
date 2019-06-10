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

package edu.northeastern.ccs.im.message;

import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.UserService;

/**
 * Special message that asks the server to edit the profile of a user.
 */
public class EditProfileMessage extends Message {

  /**
   * Constructor for the edit profile message.
   *
   * @param property the property to change
   * @param text     the value to change to
   */
  EditProfileMessage(String property, String text) {
    super(MessageType.EDIT, property, text);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    String result = UserService.updateProfile(clientRunnable.getName(), getName(), getText());
    clientRunnable.enqueueMessage(MessageFactory.makeNotificationMessage(result));
    return true;
  }
}
