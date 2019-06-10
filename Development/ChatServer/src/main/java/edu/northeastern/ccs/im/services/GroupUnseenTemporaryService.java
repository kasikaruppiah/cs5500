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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.GroupUnseenTemporary;

/**
 * This class contains all the methods to perform action on the group unseen temporary service.
 */
public class GroupUnseenTemporaryService {
    private static EntityManager em = EntityManagerResource.getEntityManager();

    /**
     * Private constructor to prohibit creating objects of this class.
     */
    private GroupUnseenTemporaryService() {
    }

    /**
     * Creates an instance of the group unseen temporary object and persists that object onto the
     * server.
     *
     * @param groupId    reference to the group.
     * @param receiverId reference to the receiver of the message
     * @param messageId  reference to the message
     * @return GroupUnseenTemporary object created.
     */
    public static GroupUnseenTemporary createUnseenGroupMessage(Long groupId, Long receiverId,
                                                                Long messageId) {
        if (messageId != null) {
            GroupUnseenTemporary groupUnseenTemporary = new GroupUnseenTemporary(groupId, receiverId,
                    messageId);

            EntityManagerResource.persist(groupUnseenTemporary);

            return groupUnseenTemporary;
        } else {
            return null;
        }
    }

    /**
     * This method is for deleting group unseen entries for a group.
     *
     * @param group group
     * @return true if entries deleted, false otherwise
     */
    static boolean deleteGroupUnseenForGroup(Group group) {
        Query query = em.createQuery("SELECT unseen FROM GroupUnseenTemporary unseen WHERE groupId = :groupId");
        query.setParameter("groupId", group.getId());
        EntityManagerResource.removeAll(query.getResultList());

        return true;
    }
}
