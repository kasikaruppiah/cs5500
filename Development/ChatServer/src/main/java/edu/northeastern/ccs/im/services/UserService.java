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

import edu.northeastern.ccs.im.models.GroupParticipation;
import edu.northeastern.ccs.im.models.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This class is a user service for the user. This contains method for performing actions on a
 * user.
 */
public class UserService {

  private static EntityManager em = EntityManagerResource.getEntityManager();
  private static String usernameField = "username";

  /**
   * Private constructor to prohibit creating objects of this class.
   */
  private UserService() {

  }

  /**
   * Method to check credentials of a user against the database.
   *
   * @param username username of the user
   * @param password password of the user
   * @return true if correct credentials, false otherwise
   */
  public static boolean checkCredentials(String username, String password) {
    User user = findUser(username);

    if (user == null) {
      return false;
    }

    return BCrypt.checkpw(password, user.getPassword());
  }

  /**
   * Method to get the profile of a user given the username.
   *
   * @param username username of the user
   * @return profile of the user as a string
   */
  public static String getProfile(String username) {
    User user = findUser(username);
    if (user == null) {
      return null;
    } else {
      return user.toString();
    }
  }

  /**
   * Method of registering user into the system using the username and password.
   *
   * @param username username of the user
   * @param password password of the user
   * @return true if registration successful, false otherwise
   */
  public static boolean registerUser(String username, String password) {
    return createUser(username, password, "", "", "");
  }

  /**
   * Method for creating a new user in the database.
   *
   * @param username username of the user
   * @param password password of the user
   * @param firstName first name of the user
   * @param lastName last name of the user
   * @param email email of the user
   * @return true if user creation successful, false otherwise
   */
  public static boolean createUser(String username, String password, String firstName,
      String lastName, String email) {

    try {
      EntityManagerResource.persist(new User(username, password, firstName, lastName, email));
      return true;
    } catch (PersistenceException exception) {
      return false;
    }
  }

  public static String updateProfile(String username, String parameter, String newValue) {
    String result = "Unable to find the user";
    User user = findUser(username);

    if (user != null) {
      result = "Changes the " + parameter + " successfully";
      switch (parameter) {
        case "firstName":
          user.setFirstName(newValue);
          break;
        case "lastName":
          user.setLastName(newValue);
          break;
        case "email":
          user.setEmail(newValue);
          break;
        case "password":
          user.setPassword(newValue);
          break;
        default:
          result = "Field to change is not recognized : ".concat(parameter);
          break;
      }
      EntityManagerResource.persist(user);
    }
    return result;
  }

  /**
   * Find and return a user with specified username. Returns null if the user is not found
   *
   * @param username the username to search
   * @return nulls if the user is not found else user
   */
  public static User findUser(String username) {
    List<User> userList = em.createNamedQuery("User.find", User.class)
        .setParameter(usernameField, username)
        .getResultList();
    if (userList.isEmpty()) {
      return null;
    } else {
      return userList.get(0);
    }
  }

  /**
   * Find if user if part of a group
   *
   * @param user the user to search
   * @param groupname the group name to filter
   */
  public static Boolean checkUserinGroup(User user, String groupname) {
    for (GroupParticipation groupParticipation : user.getGroups()) {
      if (groupParticipation.getGroup().getGroupname().equals(groupname)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the list of all groups for the username specified
   *
   * @param username the username to search
   * @return the list of groups of the user
   */
  public static String getListOfGroups(String username) {
    User user = findUser(username);
    if (user == null) {
      return "User not found";
    }
    if (username != null) {
      List<GroupParticipation> groupList = em
          .createNamedQuery("GroupParticipation.findGroupsOfUser", GroupParticipation.class)
          .setParameter("user", user).getResultList();
      if (!groupList.isEmpty()) {
        StringBuilder result = new StringBuilder();
        result.append("Groups: \n");
        for (GroupParticipation part : groupList) {
          result.append(part.getGroup().toString()).append("\n");
        }
        return result.toString();
      }
    }
    return "No Groups for this user";
  }

  public static String getFollowing(User user) {
    return "Following : [ " + String.join(", ",
        user.getFollowing().stream().map(iUser -> "$" + iUser.getUsername())
            .collect(Collectors.toList())) + " ]";
  }

  public static void tagUser(User user, User tagUser) {
    user.tagUser(tagUser);

    EntityManagerResource.persist(user);
  }

  public static void untagUser(User user, User untagUser) {
    user.untagUser(untagUser);

    EntityManagerResource.persist(user);
  }
}
