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

import edu.northeastern.ccs.im.MessageStrings;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * The search message is the request from the user
 */
public class SearchMessage extends Message {
  private String query;
  private String parameter;

  /**
   * Constructor for the search message.
   *
   * @param query     the search query
   * @param paramater the search parameter
   */
  public SearchMessage(String query, String paramater) {
    super(MessageType.SEARCH, query, paramater);
    this.query = query;
    this.parameter = paramater;
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    if (this.query.equalsIgnoreCase("user")) {
      User found = UserService.findUser(this.parameter);
      if (found != null) {
        clientRunnable.enqueueMessage(MessageFactory.makeNotificationMessage(found.toString()));
      } else {
        clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_USER_EXIST.toString()));
      }
    } else if (this.query.equalsIgnoreCase("group")) {
      Group group = GroupService.findGroup(this.parameter);
      if (group != null) {
        clientRunnable.enqueueMessage(MessageFactory.makeNotificationMessage(group.toString()));
      } else {
        clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
      }
    } else {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.INVALID_QUERY_PARAM.toString()));
    }
    return true;
  }
}
