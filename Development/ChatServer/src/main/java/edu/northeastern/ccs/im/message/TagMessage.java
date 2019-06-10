package edu.northeastern.ccs.im.message;

import static edu.northeastern.ccs.im.services.UserService.findUser;
import static edu.northeastern.ccs.im.services.UserService.tagUser;

import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;

public class TagMessage extends Message {

  /**
   * Create a new message that contains actual IM text. The type of distribution is defined by the
   * handle and we must also set the name of the message sender, message recipient, and the text to
   * send.
   *
   * @param srcName Name of the individual sending this message
   */
  TagMessage(String srcName) {
    super(MessageType.TAG, srcName, null);
  }

  /**
   * Dispatch boolean.
   *
   * @param clientRunnable the client runnable
   * @return the boolean
   */
  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    Message notMessage;

    String username = getName();

    if (username.equals(clientRunnable.getName())) {
      notMessage = MessageFactory.makeNotificationMessage("Self Tag not allowed");
    } else {
      User tagUser = findUser(username);

      if (tagUser != null) {
        User user = clientRunnable.getUser();

        if (!user.getFollowing().contains(tagUser)) {
          tagUser(user, tagUser);
          notMessage = MessageFactory
              .makeNotificationMessage("Successfully following user $" + username);
        } else {
          notMessage = MessageFactory
              .makeNotificationMessage("Already following user $" + username);
        }
      } else {
        notMessage = MessageFactory.makeNotificationMessage("Not a valid user $" + username);
      }
    }

    clientRunnable.enqueueMessage(notMessage);

    return true;
  }
}