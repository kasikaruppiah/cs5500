package edu.northeastern.ccs.im.message;

import static edu.northeastern.ccs.im.services.UserService.findUser;
import static edu.northeastern.ccs.im.services.UserService.tagUser;
import static edu.northeastern.ccs.im.services.UserService.untagUser;

import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;

public class UnTagMessage extends Message {

  /**
   * Create a new message that contains actual IM text. The type of distribution is defined by the
   * handle and we must also set the name of the message sender, message recipient, and the text to
   * send.
   *
   * @param srcName Name of the individual sending this message
   */
  UnTagMessage(String srcName) {
    super(MessageType.UNTAG, srcName, null);
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

    User untagUser = findUser(username);

    if (untagUser != null) {
      User user = clientRunnable.getUser();

      if (user.getFollowing().contains(untagUser)) {
        untagUser(user, untagUser);
        notMessage = MessageFactory
            .makeNotificationMessage("Successfully unfollowed user $" + username);
      } else {
        notMessage = MessageFactory.makeNotificationMessage("Not following user $" + username);
      }
    } else {
      notMessage = MessageFactory.makeNotificationMessage("Not a valid user $" + username);
    }

    clientRunnable.enqueueMessage(notMessage);

    return true;
  }
}
