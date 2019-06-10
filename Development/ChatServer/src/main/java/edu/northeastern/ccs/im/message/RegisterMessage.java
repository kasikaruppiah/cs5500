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
 * This is a Register message. This message is used for registering a user in the system by a
 * username and password.
 */
public class RegisterMessage extends Message {
  /**
   * This constructs a message of the type Register message.
   *
   * @param username username of the user to be registered
   * @param password password of the user to be registered
   */
  RegisterMessage(String username, String password) {
    super(MessageType.REGISTER, username, password);
  }

  @Override
  public boolean isInitialization() {
    return true;
  }


  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    if (UserService.registerUser(getName(), getText())) {
      clientRunnable.enqueueMessage(MessageFactory.makeAcknowledgeMessage(getName()));
      return true;
    } else {
      return false;
    }
  }
}
