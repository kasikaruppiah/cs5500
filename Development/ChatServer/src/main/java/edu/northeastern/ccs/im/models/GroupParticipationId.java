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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * The type Group participation id.
 */
@Embeddable
public class GroupParticipationId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;

    /**
     * Instantiates a new Group participation id.
     */
    public GroupParticipationId() {
    }

    /**
     * Instantiates a new Group participation id.
     *
     * @param userId  the user id
     * @param groupId the group id
     */
    public GroupParticipationId(Long userId, Long groupId) {
        setUserId(userId);
        setGroupId(groupId);
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets group id.
     *
     * @param groupId the group id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupParticipationId that = (GroupParticipationId) o;
        return getUserId().equals(that.getUserId()) &&
                getGroupId().equals(that.getGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getGroupId());
    }
}
