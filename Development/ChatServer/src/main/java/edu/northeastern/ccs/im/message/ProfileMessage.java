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
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * Profile message is the client requesting profile for some user in the system or his profile.
 */
public class ProfileMessage extends Message {

  /**
   * Constructor for the profile message request.
   *
   * @param srcName The profile request.
   */
  ProfileMessage(String srcName) {
    super(MessageType.PROFILE, srcName, null);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    String profile = "";
    if (clientRunnable.getName().equalsIgnoreCase(getName())) {
      profile = UserService.getProfile(getName());
      String followingUsers = UserService.getFollowing(clientRunnable.getUser());
      profile = profile.concat("\n" + followingUsers);
      String groupsOfUser = UserService.getListOfGroups(getName());
      profile = profile.concat("\n");
      profile = profile.concat(groupsOfUser);
    } else if (getName().startsWith("$")) {
      profile = UserService.getProfile(getName().substring(1));
      if (profile == null) {
        clientRunnable.enqueueMessage(
            MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_USER_EXIST.toString()));
        return true;
      }
    } else if (getName().startsWith("@")) {
      profile = GroupService.getGroupProfile(getName().substring(1));
      if (profile == null) {
        clientRunnable.enqueueMessage(
            MessageFactory.makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
        return false;
      }
      profile = profile.concat("/n");
      profile = profile.concat(GroupService.findGroup(getName().substring(1)).toString());
    } else {
      clientRunnable.enqueueMessage(
          MessageFactory.makeNoAcknowledgeMessage(MessageStrings.INVALID_COMMAND_NOACK.toString()));
      return true;
    }
    clientRunnable.enqueueMessage(MessageFactory.makeNotificationMessage(profile));
    return true;
  }
}
