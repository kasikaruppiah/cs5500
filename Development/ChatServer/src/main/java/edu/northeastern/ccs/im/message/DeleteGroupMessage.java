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

import java.util.Objects;

import edu.northeastern.ccs.im.MessageStrings;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * The type Delete group message.
 */
public class DeleteGroupMessage extends Message {


  /**
   * Instantiates a new Delete group message.
   *
   * @param srcName   the src name
   * @param groupname the groupname
   */
  DeleteGroupMessage(String srcName, String groupname) {
    super(MessageType.DELETE_GROUP, srcName, groupname);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    String groupname = getText();

    if (GroupService.findGroup(groupname) == null) {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
    } else if (GroupService.deleteGroup(UserService.findUser(getName()), groupname)) {
      clientRunnable.enqueueMessage(MessageFactory.makeAcknowledgeMessage(MessageStrings.DELETE_GROUP_ACK.toString()));
    } else if (Objects.requireNonNull(GroupService.findGroup(groupname)).getModerators().stream().noneMatch(moderator -> moderator.equals(UserService.findUser(getName())))) {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.GROUP_MODERATOR_ACTION_NOACK.toString()));
    } else {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.DELETE_GROUP_INVALID_COMMAND.toString()));
    }
    return true;
  }
}
