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

import static edu.northeastern.ccs.im.services.GroupService.findGroup;
import static edu.northeastern.ccs.im.services.UserService.checkUserinGroup;
import static edu.northeastern.ccs.im.services.UserService.findUser;

import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.GroupUnseenTemporary;
import edu.northeastern.ccs.im.models.MessageModel;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.EntityManagerResource;
import edu.northeastern.ccs.im.services.MessageService;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Open message requests the server to open a chat. A chat can be a personal chat or a group chat.
 */
public class OpenMessage extends Message {


  /**
   * Instantiates a new Open message.
   *
   * @param srcName the name of the user/group to be opened
   */
  OpenMessage(String srcName) {
    super(MessageType.OPEN, srcName, null);
  }

  /**
   * Dispatch boolean.
   *
   * @param clientRunnable the client runnable
   * @return the boolean
   */
  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    User user = findUser(clientRunnable.getName());
    String requestName = getName();
    Message notMessage;

    if (requestName.startsWith("$")) {
      String username = requestName.substring(1);

      if (findUser(username) != null) {
        notMessage = openPrivateMessage(clientRunnable.getName(), username);
      } else {
        notMessage = MessageFactory.makeNotificationMessage("Not a valid user $" + username);
      }
    } else {
      String groupname = requestName.substring(1);

      if (findGroup(groupname) != null) {
        if (checkUserinGroup(user, groupname)) {
          notMessage = openGroupMessage(clientRunnable.getName(), groupname);
        } else {
          notMessage = MessageFactory.makeNotificationMessage(
              "$" + user.getUsername() + " is not a member of @" + groupname);
        }
      } else {
        notMessage = MessageFactory.makeNotificationMessage("Not a valid group @" + groupname);
      }
    }

    clientRunnable.enqueueMessage(notMessage);

    return true;
  }

  /**
   * Method to open private messages for a user
   * @param sender the sender of a message
   * @param receiver the receiver of a message
   * @return
   */
  private Message openPrivateMessage(String sender, String receiver) {
    Map<String, Object> chatObj = MessageService.openChat(sender, receiver);
    Date seenTimeStamp = new Date();

    Message notMessage = MessageFactory
        .makeNotificationMessage((String) chatObj.get("messageText"));

    List<MessageModel> messages = (List<MessageModel>) chatObj.get("unreadMessages");
    if (!messages.isEmpty()) {
      for (MessageModel message : messages) {
        message.setSeenTimeStamp(seenTimeStamp);
      }

      EntityManagerResource.persistAll(messages);
    }

    return notMessage;
  }

  /**
   * Method to open the chat for a group
   *
   * @param sender the sender of the message
   * @param groupname the group
   * @return
   */
  private Message openGroupMessage(String sender, String groupname) {
    Map<String, Object> chatObj = MessageService.openGroupChat(sender, groupname);
    Message notMessage = MessageFactory
        .makeNotificationMessage((String) chatObj.get("messageText"));

    List<GroupUnseenTemporary> messages = (List<GroupUnseenTemporary>) chatObj
        .get("unreadMessages");
    if (!messages.isEmpty()) {
      EntityManagerResource.removeAll(messages);
    }

    return notMessage;
  }
}
