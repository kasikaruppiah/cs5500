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

import java.util.ArrayList;
import java.util.Arrays;

import edu.northeastern.ccs.im.MessageStrings;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * Represents message for creation of a new group in the database.
 */
public class NewGroupMessage extends Message {

  /**
   * Constructs a new group
   *
   * @param srcName   name of the creator of the group
   * @param groupname name of the group
   */
  NewGroupMessage(String srcName, String groupname) {
    super(MessageType.NEW_GROUP, srcName, groupname);
  }

  @Override
  public boolean dispatch(ClientRunnable clientRunnable) {
    User user = UserService.findUser(clientRunnable.getName());
    String text = getText();

    String[] tokens = text.split(" ");
    ArrayList<String> tokensText = new ArrayList<>(Arrays.asList(tokens));
    boolean approval = true;
    String groupname = "";
    if (tokensText.get(tokensText.size() - 1).equals("yes") || tokensText.get(tokensText.size() - 1).equals("no")) {
      approval = tokensText.remove(tokensText.size() - 1).equals("yes");
      groupname = String.join(" ", tokensText);
    } else {
      groupname = text;
    }

    if (GroupService.createGroup(groupname, approval, user)) {
      clientRunnable.enqueueMessage(MessageFactory.makeAcknowledgeMessage(MessageStrings.CREATE_GROUP_ACK.toString()));
    } else {
      clientRunnable.enqueueMessage(MessageFactory.makeNoAcknowledgeMessage(MessageStrings.CREATE_GROUP_NOACK.toString()));
    }
    return true;
  }
}
