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
import edu.northeastern.ccs.im.server.Prattle;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

/**
 * The type Group invite message.
 */
public class GroupInviteMessage extends Message {

    /**
     * Instantiates a new Group invite message that
     * sends an invite to the moderator of the given group.
     *
     * @param groupname the groupname the group to be invited for
     * @param username  the username the user to be invited to join the group
     */
    GroupInviteMessage(String groupname, String username) {
        super(MessageType.INVITE_PARTICIPANT_GROUP, groupname, username);
    }


    @Override
    public boolean dispatch(ClientRunnable clientRunnable) {
        Group group = GroupService.findGroup(getName());
        User invitee = UserService.findUser(getText());
        if (group == null) {
            clientRunnable.enqueueMessage(MessageFactory.
                    makeNoAcknowledgeMessage(MessageStrings.NO_GROUP_EXIST.toString()));
            return false;
        }
        if (invitee == null) {
            clientRunnable.enqueueMessage(MessageFactory.
                    makeNoAcknowledgeMessage(MessageStrings.NO_USER_EXIST.toString()));
            return false;
        }
        for (User moderator : group.getModerators()) {
            Prattle.privateMessage(MessageFactory.
                    makeNotificationMessage("Request from "+clientRunnable.getName() + " to add " +invitee.getUsername()
                            +" to "+ group.getGroupname()),
                    moderator.getUsername());
        }
        return true;
    }
}
