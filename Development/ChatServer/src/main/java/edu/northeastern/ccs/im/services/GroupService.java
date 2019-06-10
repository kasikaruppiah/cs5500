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

package edu.northeastern.ccs.im.services;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.User;

/**
 * Contains methods for performing actions on a group.
 */
public class GroupService {

    private static EntityManager em = EntityManagerResource.getEntityManager();

    /**
     * Private constructor to prohibit creating objects of this class.
     */
    private GroupService() {

    }

    /**
     * Method for creating a group in the database.
     *
     * @param groupname name of the group
     * @param approval  group requires approval to enter
     * @param moderator moderator of the group
     * @return true if group created successfully
     */
    public static boolean createGroup(String groupname, Boolean approval, User moderator) {
        try {
            Group newGroup = new Group(groupname, approval, moderator);
            EntityManagerResource.persist(newGroup);
            newGroup.addParticipants(moderator);
            EntityManagerResource.persist(newGroup);
            return true;
        } catch (PersistenceException | NullPointerException exception) {
            return false;
        }
    }

    /**
     * Method for deleting the group in the database.
     *
     * @param user      the user
     * @param groupname name of the group
     * @return true if successfully deleted the group
     */
    public static boolean deleteGroup(User user, String groupname) {
        Group group = findGroup(groupname);
        if (group == null) {
            return false;
        }
        if (!group.isModerator(user)) {
            return false;
        }

        try {
            GroupUnseenTemporaryService.deleteGroupUnseenForGroup(group);
            MessageService.deleteMessagesForGroup(group);
            group.removeAllParticipants();
            group.removeModerator(user);
            Group g = GroupService.findGroup(groupname);
            EntityManagerResource.remove(g);

            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Group WHERE groupname = :groupname");
            query.setParameter("groupname", Objects.requireNonNull(g).getGroupname());
            em.getTransaction().commit();
            return true;

        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Method for searching a group in the database.
     *
     * @param groupname name of the group
     * @return group if exists in database
     */
    public static Group findGroup(String groupname) {
        List<Group> userList = em.createNamedQuery("Group.find", Group.class)
                .setParameter("groupname", groupname).getResultList();
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }

    }

    /**
     * Add participant boolean.
     *
     * @param group       the group
     * @param participant the participant
     * @return the boolean
     */
    public static boolean addParticipant(Group group, User participant) {
        group.addParticipants(participant);
        EntityManagerResource.persist(group);
        return true;
    }

    /**
     * Delete participant boolean.
     *
     * @param group       the group
     * @param participant the participant
     * @return the boolean
     */
    public static boolean deleteParticipant(Group group, User participant) {
        try {
            UserService.findUser(participant.getUsername());
        } catch (NullPointerException exception) {
            return false;
        }
        if (!group.hasUser(participant)) {
            return false;
        }
        try {
            group.removeParticipants(participant);
            EntityManagerResource.persist(group);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Return the profile of the group name. The profile of a group is just the group name as of now.
     *
     * @param groupName The group name for profile search
     * @return the group profile
     */
    public static String getGroupProfile(String groupName) {
        Group group = findGroup(groupName);
        if (group == null) {
            return null;
        } else {
            return "Group name: " + group.getGroupname() + " {Users: " + group.getGroupParticipantsUsernames() + "}";
        }
    }

    /**
     * Edit the group name of the group provided.
     *
     * @param group   the group whose name is to be changed
     * @param newName the new name of the group.
     */
    public static boolean editGroupName(Group group, String newName) {
        try {
            group.setGroupname(newName);
            EntityManagerResource.persist(group);
            return true;
        } catch (Exception e) {
            ChatLogger.warning(e.toString());
            return false;
        }
    }

    /**
     * Update the approval setting for the group
     *
     * @param group the group to change setting for
     */
    public static boolean updateApproval(Group group) {
        try {
            group.setApproval(!group.getApproval());
            EntityManagerResource.persist(group);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
