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
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * Special message that asks the server to change the approval setting of the group.
 */
public class EditGroupApprovalMessage extends Message {


  /**
   * Instantiates a new Edit group approval message.
   *
   * @param groupName the group name of the group
   */
  EditGroupApprovalMessage(String groupName) {
    super(MessageType.GROUP_APPROVAL_EDIT, groupName, null);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    Group group = GroupService.findGroup(this.getName());
    if (group == null) {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
    } else {
      if (group.getModerators().contains(UserService.findUser(clientRunnable.getName()))) {
        if (GroupService.updateApproval(group)) {
          clientRunnable.enqueueMessage(MessageFactory.makeAcknowledgeMessage(MessageStrings.EDIT_GROUP_APPROVAL_ACK.toString()));
        } else {
          clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.EDIT_GROUP_APPROVAL_NOACK.toString()));
        }
      } else {
        clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.GROUP_MODERATOR_ACTION_NOACK.toString()));
      }
    }
    return true;
  }
}
