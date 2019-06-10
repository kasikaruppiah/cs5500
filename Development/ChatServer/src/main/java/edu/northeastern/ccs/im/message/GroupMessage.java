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
import edu.northeastern.ccs.im.models.GroupParticipation;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.Prattle;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.MessageService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * Group message is a message sent within a group. Every group message has a sender and the message
 * text.
 */
public class GroupMessage extends Message {

  /**
   * Constructor for the group message.
   *
   * @param srcName the sender of the group message
   * @param text the message body
   */
  GroupMessage(String srcName, String text) {
    super(MessageType.GROUP, srcName, text);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    User sender = UserService.findUser(clientRunnable.getName());
    Group group = GroupService.findGroup(getName());

    if (group == null) {
      clientRunnable.enqueueMessage(
          MessageFactory.makeNotificationMessage(MessageStrings.NO_GROUP_EXIST.toString()));
      return false;
    }

    if (!UserService.checkUserinGroup(sender, group.getGroupname())) {
      clientRunnable.enqueueMessage(
          MessageFactory.makeNotificationMessage(MessageStrings.GROUP_MESSAGE_NOACK.toString()));
      return false;
    }

    MessageService.createGroupMessage(sender, group, getText(), getHandle().toString());

    for (GroupParticipation participant : group.getParticipants()) {
      if (!participant.getUser().equals(sender)) {
        Prattle.privateMessage(MessageFactory
                .makeGroupMessage(clientRunnable.getName() + "@" + group.getGroupname(), getText()),
            participant.getUser().getUsername());
      }
    }

    return true;

  }
}
