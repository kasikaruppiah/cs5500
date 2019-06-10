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
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.NaturalId;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This is the user model for the database with the name USER_TABLE. This model represents data
 * about the user.
 */
@Entity
@Table(name = "USER_TABLE")
@NamedQueries({
    @NamedQuery(name = "User.find", query = "SELECT user FROM User user WHERE user.username = :username"),
    @NamedQuery(name = "User.findByCredentials", query = "SELECT user FROM User user WHERE user.username = :username and user.password = :password")
})
public class User implements Serializable {

  /**
   * Primary indentifier of a user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Unique username of a user.
   */
  @NaturalId
  @Column(unique = true, nullable = false)
  private String username;

  /**
   * Password of a user.
   */
  private String password;

  /**
   * First name of a user.
   */
  private String firstName;

  /**
   * Last name of a user.
   */
  private String lastName;

  /**
   * Email of a user.
   */
  private String email;

  @Basic(optional = false)
  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTimeStamp;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<GroupParticipation> groups;

  @ManyToMany(mappedBy = "moderators")
  private Set<Group> moderatedGroups;

  @ManyToMany
  private Set<User> following;

  /**
   * Default constructor of a user. Required for database access.
   */
  public User() {
  }

  /**
   * This constructs a user objects with the details of the user.
   *
   * @param username  unique username of the user for the system
   * @param password  password of the user for the system
   * @param firstName first name of the user
   * @param lastName  last name of the user
   * @param email     email of the user
   */
  public User(String username, String password, String firstName, String lastName, String email) {
    setUsername(username);
    setPassword(password);
    setFirstName(firstName);
    setLastName(lastName);
    setEmail(email);
    setCreatedTimeStamp(new Date());
    setModeratedGroups(new HashSet<>());
    setGroups(new HashSet<>());
    setFollowing(new HashSet<>());
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
   * Set id
   *
   * @param id the id
   */
  void setId(Long id) {
    this.id = id;
  }

  /**
   * Getter method for username of a user.
   *
   * @return username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter method for username of a user.
   *
   * @param username username of the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter method for password of a user.
   *
   * @return password of a user
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Setter method of password of a user.
   *
   * @param password password of a user
   */
  public void setPassword(String password) {
    this.password = BCrypt.hashpw(password, BCrypt.gensalt());
  }

  /**
   * Getter method for first name of a user.
   *
   * @return first name of a user
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Setter method for first name of a user
   *
   * @param firstName first name of a user
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Getter method for last name of a user
   *
   * @return last name of a user
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Setter method for last name of a user
   *
   * @param lastName last name of a user
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Getter method for email of a user
   *
   * @return email of a user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Setter method for email of a user
   *
   * @param email email of a user
   */
  public void setEmail(String email) {
    this.email = email;
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
   * Setter method for the timestamp when the user was created
   *
   * @param createdTimeStamp the timestamp when the user was created
   */
  private void setCreatedTimeStamp(Date createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  /**
   * Gets moderated groups.
   *
   * @return the moderated groups
   */
  public Set<Group> getModeratedGroups() {
    return moderatedGroups;
  }

  /**
   * Sets moderated groups.
   *
   * @param moderatedGroups the moderated groups
   */
  public void setModeratedGroups(Set<Group> moderatedGroups) {
    this.moderatedGroups = moderatedGroups;
  }

  /**
   * Sets groups.
   *
   * @param groups the groups
   */
  public void setGroups(Set<GroupParticipation> groups) {
    this.groups = groups;
  }

  /**
   * Gets the set of groups the user is a part of
   *
   * @return the set of groups the user is a part of
   */
  public Set<GroupParticipation> getGroups() {
    return groups;
  }

  public void setFollowing(Set<User> following) {
    this.following = following;
  }

  public Set<User> getFollowing() {
    return following;
  }

  public void tagUser(User user) {
    this.following.add(user);
  }

  public void untagUser(User user) {
    this.following.remove(user);
  }

  @Override
  public String toString() {
    return " username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'';
  }
}