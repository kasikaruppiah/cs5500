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
 * Add participant to group message
 */
public class AddParticipantGroupMessage extends Message {

  /**
   * Instantiates a new Add participant group message.
   *
   * @param groupname the groupname of the group to which the user is to be added
   * @param username  the username of the user to be added
   */
  AddParticipantGroupMessage(String groupname, String username) {
    super(MessageType.ADD_PARTICIPANT_GROUP, groupname, username);
  }


  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    Group group = GroupService.findGroup(getName());
    if (group == null) {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
      return false;
    }
    User user = UserService.findUser(clientRunnable.getName());
    User participant = UserService.findUser(getText());
    if (participant == null) {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_USER_EXIST.toString()));
      return false;
    }
    if (UserService.checkUserinGroup(participant, group.getGroupname())) {
      clientRunnable.enqueueMessage(
              MessageFactory.makeNotificationMessage(MessageStrings.USER_EXIST_IN_GROUP.toString()));
      return false;
    }

    if (!UserService.checkUserinGroup(user, group.getGroupname())) {
      clientRunnable.enqueueMessage(
              MessageFactory.makeNotificationMessage(MessageStrings.GROUP_MESSAGE_NOACK.toString()));
      return false;
    }

    if (!group.getApproval() || group.getModerators().stream().anyMatch(moderator -> moderator.equals(user))) {
      GroupService.addParticipant(group, participant);
      clientRunnable.enqueueMessage(MessageFactory.makeAcknowledgeMessage(MessageStrings.ADD_USER_ACK.toString()));
      return true;
    } else {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.ADD_USER_NOACK.toString()));
      return false;
    }
  }
}
