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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The type Message model.
 */
@Entity
@Table(name = "MESSAGE_TABLE")

@NamedQueries({
        @NamedQuery(name = "Message.findAll", query = "SELECT message FROM MessageModel message"),
        @NamedQuery(name = "Message.findGroupMessages", query = "SELECT message FROM MessageModel message WHERE message.group = :group"),

})
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", referencedColumnName = "username")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "groupname", referencedColumnName = "groupname")
    private Group group;

    private String messageText;

    private String messageType;

    @Basic(optional = false)
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimeStamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date seenTimeStamp;

    public MessageModel() {
    }

    /**
     * Instantiates a new Message model.
     *
     * @param sender      the sender
     * @param receiver    the receiver
     * @param group       the group
     * @param messageText the message text
     * @param messageType the message type
     */
    public MessageModel(User sender, User receiver, Group group, String messageText, String messageType) {
        setSender(sender);
        setReceiver(receiver);
        setGroup(group);
        setMessageText(messageText);
        setMessageType(messageType);
        setCreatedTimeStamp(new Date());
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
    void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets message text.
     *
     * @return the message text
     */
    public String getMessageText() {
        return messageText;
    }

    private void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Gets created time stamp.
     *
     * @return the created time stamp
     */
    public Date getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    private void setCreatedTimeStamp(Date createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    /**
     * Gets seen time stamp.
     *
     * @return the seen time stamp
     */
    public Date getSeenTimeStamp() {
        return seenTimeStamp;
    }

    /**
     * Sets seen time stamp.
     *
     * @param seenTimeStamp the seen time stamp
     */
    public void setSeenTimeStamp(Date seenTimeStamp) {
        this.seenTimeStamp = seenTimeStamp;
    }

    /**
     * Gets message type.
     *
     * @return the message type
     */
    public String getMessageType() {
        return messageType;
    }

    private void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets sender.
     *
     * @param sender the sender
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets receiver.
     *
     * @return the receiver
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets receiver.
     *
     * @param receiver the receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
}