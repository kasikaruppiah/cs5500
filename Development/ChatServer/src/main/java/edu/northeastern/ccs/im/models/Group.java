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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The type Group.
 */
@Entity
@Table(name = "GROUP_TABLE")
@NamedQueries({
        @NamedQuery(name = "Group.find", query = "SELECT group FROM Group group WHERE group.groupname = :groupname"),

})
public class Group implements Serializable {
  /**
   * Primary identifier of a group
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Name of the group
   */
  @Column(unique = true, nullable = false)
  private String groupname;

  /**
   * If this group requires approval to enter
   */
  private Boolean approval;

  /**
   * The timestamp when the group was created
   */
  @Basic(optional = false)
  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTimeStamp;

  /**
   * The set of participants in the group
   */
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GroupParticipation> participants;

  /**
   * The set of moderators for the group
   */
  @ManyToMany
  @JoinTable(
          name = "GROUP_MODERATORS",
          joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "moderator_id", referencedColumnName = "id")
  )
  private Set<User> moderators;

  /**
   * Instantiates a new Group.
   */
  public Group() {
  }

  /**
   * Instantiates a new Group.
   *
   * @param groupname the groupname
   * @param approval  the approval
   * @param moderator the moderator of the group
   */
  public Group(String groupname, Boolean approval, User moderator) {
    setGroupname(groupname);
    setModerators(new HashSet<>());
    addModerator(moderator);
    setApproval(approval);
    setCreatedTimeStamp(new Date());
    setParticipants(new HashSet<>());
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
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Gets groupname.
   *
   * @return the groupname
   */
  public String getGroupname() {
    return groupname;
  }

  /**
   * Sets groupname.
   *
   * @param groupname the groupname
   */
  public void setGroupname(String groupname) {
    this.groupname = groupname;
  }


  /**
   * Gets approval.
   *
   * @return the approval
   */
  public Boolean getApproval() {
    return approval;
  }

  /**
   * Sets approval.
   *
   * @param approval the approval
   */
  public void setApproval(Boolean approval) {
    this.approval = approval;
  }

  /**
   * Gets created time stamp.
   *
   * @return the created time stamp
   */
  public Date getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  /**
   * Sets created time stamp.
   *
   * @param createdTimeStamp the created time stamp
   */
  public void setCreatedTimeStamp(Date createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  /**
   * Gets participants.
   *
   * @return the participants
   */
  public Set<GroupParticipation> getParticipants() {
    return participants;
  }

  /**
   * Checks if the group has the given user.
   *
   * @param user user to check
   * @return true if user exists, false otherwise
   */
  public boolean hasUser(User user) {
    for (GroupParticipation part : this.getParticipants()) {
      if (part.getUser().equals(user)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets participants.
   *
   * @param participants the participants
   */
  public void setParticipants(Set<GroupParticipation> participants) {
    this.participants = participants;
  }

  /**
   * Gets moderators.
   *
   * @return the moderators
   */
  public Set<User> getModerators() {
    return moderators;
  }

  /**
   * Sets moderators.
   *
   * @param moderators the moderators
   */
  public void setModerators(Set<User> moderators) {
    this.moderators = moderators;
  }

  /**
   * Add moderator.
   *
   * @param moderator the moderator
   */
  public void addModerator(User moderator) {
    this.moderators.add(moderator);
    moderator.getModeratedGroups().add(this);
  }

  /**
   * Remove moderator.
   *
   * @param moderator the moderator
   */
  public void removeModerator(User moderator) {
    this.moderators.remove(moderator);
    moderator.getModeratedGroups().remove(this);
  }

  /**
   * Add participants.
   *
   * @param user the user
   */
  public void addParticipants(User user) {
    GroupParticipation groupParticipation = new GroupParticipation(user, this, new Date());
    this.participants.add(groupParticipation);
    user.getGroups().add(groupParticipation);
  }

  /**
   * Remove participants.
   *
   * @param user the user
   */
  public void removeParticipants(User user) {
    for (Iterator<GroupParticipation> iterator = participants.iterator(); iterator.hasNext(); ) {
      GroupParticipation participation = iterator.next();
      if (participation.getUser().equals(user) && participation.getGroup().equals(this)) {
        iterator.remove();
        participation.getUser().getGroups().remove(participation);
        participation.setGroup(null);
        participation.setUser(null);
      }
    }
  }

  /**
   * Remove all participants.
   */
  public void removeAllParticipants() {
    for (Iterator<GroupParticipation> iterator = participants.iterator(); iterator.hasNext(); ) {
      GroupParticipation participation = iterator.next();
      if (participation.getGroup().equals(this)) {
        iterator.remove();
        participation.getUser().getGroups().remove(participation);
        participation.setGroup(null);
        participation.setUser(null);
      }
    }
  }

  /**
   * This method is for getting all the usernames of the moderators for this group.
   *
   * @return List of usernames for all moderators as a string
   */
  private String getModeratorUsernames() {
    StringBuilder moderatorUsernames = new StringBuilder();
    for (User user : this.moderators) {
      moderatorUsernames.append(user.getUsername()).append(", ");
    }
    String toRet;
    try {
      toRet = moderatorUsernames.deleteCharAt(moderatorUsernames.length() - 1).deleteCharAt(moderatorUsernames.length() - 1).toString();
    } catch (StringIndexOutOfBoundsException e) {
      toRet = "";
    }
    return toRet;
  }

  /**
   * This method is for checking if a user is a moderator of this group.
   *
   * @param user user to check
   * @return true if user is the moderator, false otherwise
   */
  public boolean isModerator(User user) {
    for (User u : this.moderators) {
      if (u.getUsername().equals(user.getUsername())) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method is for getting all the usernames of the moderators for this group.
   *
   * @return List of usernames for all moderators as a string
   */
  public String getGroupParticipantsUsernames() {
    StringBuilder participantUsernames = new StringBuilder();
    for (GroupParticipation groupParticipation : this.participants) {
      participantUsernames.append(groupParticipation.getUser().getUsername()).append(", ");
    }
    return participantUsernames.deleteCharAt(participantUsernames.length() - 1).deleteCharAt(participantUsernames.length() - 1).toString();
  }

  @Override
  public String toString() {
    return "Group{" +
            "groupname='" + groupname + '\'' +
            ", approval=" + approval +
            ", createdTimeStamp=" + createdTimeStamp +
            ", moderators=" + getModeratorUsernames() +
            '}';
  }
}
