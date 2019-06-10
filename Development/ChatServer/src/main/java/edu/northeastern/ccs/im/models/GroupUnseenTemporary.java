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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The type Group unseen temporary.
 */
@Entity
@Table(name = "GROUP_UNSEEN_TEMPORARY_TABLE")
@NamedQuery(name = "GroupUnseenTemporary.getAll", query = "SELECT g FROM GroupUnseenTemporary g")
public class GroupUnseenTemporary {

  /**
   * The Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;

  @Column(name = "groupId")
  private Long groupId;

  @Column(name = "receiverId")
  private Long receiverId;

  @Column(name = "messageId")
  private Long messageId;

  /**
   * Instantiates a new Group unseen temporary.
   *
   * @param groupId    the group id
   * @param receiverId the receiver id
   * @param messageId  the message id
   */
  public GroupUnseenTemporary(Long groupId, Long receiverId, Long messageId) {
    this.setGroupId(groupId);
    this.setMessageId(messageId);
    this.setReceiverId(receiverId);

  }

  /**
   * Instantiates a new Group unseen temporary.
   */
  public GroupUnseenTemporary() {
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets group id.
   *
   * @return the group id
   */
  Long getGroupId() {
    return groupId;
  }

  private void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * Gets receiver id.
   *
   * @return the receiver id
   */
  Long getReceiverId() {
    return receiverId;
  }

  private void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public Long getMessageId() {
    return messageId;
  }

  private void setMessageId(Long messageId) {
    this.messageId = messageId;
  }



}
