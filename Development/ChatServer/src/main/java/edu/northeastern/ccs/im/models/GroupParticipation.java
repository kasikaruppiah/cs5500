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

package edu.northeastern.ccs.im.models;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * The type Group participation.
 */
@Entity
@Table(name = "GROUP_PARTICIPATION")
@NamedQueries({
        @NamedQuery(name = "GroupParticipation.findGroupsOfUser", query = "SELECT groupParticipation FROM GroupParticipation groupParticipation WHERE groupParticipation.user = :user")
})
public class GroupParticipation implements Serializable {

    @EmbeddedId
    private GroupParticipationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("group_id")
    @JoinColumn(name = "group_id",referencedColumnName = "id")
    private Group group;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveTimeStamp;

    public GroupParticipation(){
    }

    public GroupParticipation(User user, Group group, Date lastActiveTimeStamp) {
        this.user = user;
        this.group = group;
        setId(new GroupParticipationId(user.getId(),group.getId()));
        this.lastActiveTimeStamp = lastActiveTimeStamp;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public GroupParticipationId getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(GroupParticipationId id) {
        this.id = id;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets last active time stamp.
     *
     * @return the last active time stamp
     */
    public Date getLastActiveTimeStamp() {
        return lastActiveTimeStamp;
    }

    /**
     * Sets last active time stamp.
     *
     * @param lastActiveTimeStamp the last active time stamp
     */
    public void setLastActiveTimeStamp(Date lastActiveTimeStamp) {
        this.lastActiveTimeStamp = lastActiveTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupParticipation that = (GroupParticipation) o;
        return getUser().equals(that.getUser()) &&
                getGroup().equals(that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getGroup());
    }
}
