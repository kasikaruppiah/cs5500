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


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupServiceTest {
  @BeforeEach
  void setup() {
    UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
    UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
  }

  @AfterEach
  void truncateTable() {
    EntityManagerHelper.truncateUsers();
    EntityManagerHelper.truncateGroups();
  }

  @Test
  void createGroupTest() {
    User user = UserService.findUser("neha");
    Assertions.assertTrue(GroupService.createGroup("Huskies 2019", true, user));
    Assertions.assertTrue(UserService.getListOfGroups("neha").contains("Groups: \n" +
            "Group{groupname='Huskies 2019'"));
  }

  @Test
  void createGroupTest2() {
    User user = UserService.findUser("jake123");
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
  }

  @Test
  void createGroupsWithSameName() {
    User user = UserService.findUser("jake123");
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
    Assertions.assertFalse(GroupService.createGroup("Huskies 2019", true, user));
  }

  @Test
  void createMultipleGroupsSameUser() {
    User user = UserService.findUser("neha");
    assertTrue(GroupService.createGroup("Huskies 2019 1", true, user));
    assertTrue(GroupService.createGroup("Huskies 2019 2", true, user));
    assertTrue(GroupService.createGroup("Huskies 2019 3", true, user));
  }

  @Test
  void createGroupInvalidUser() {
    User user = UserService.findUser("ruturaj");
    Assertions.assertFalse(GroupService.createGroup("Huskies 2019", true, user));
  }

  @Test
  void createGroupApprovalFalse() {
    User user = UserService.findUser("neha");
    assertTrue(GroupService.createGroup("Huskies 2019", false, user));
  }

  @Test
  void createGroupAddParticipants() {
    User user = UserService.findUser("neha");
    User user1 = UserService.findUser("jake123");
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
    Group group = GroupService.findGroup("Huskies 2019");
    assertNotNull(group);
    assertTrue(GroupService.addParticipant(group, user1));
  }

  @Test
  void deleteGroup() {
    User user = UserService.findUser("neha");
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
    Group group = GroupService.findGroup("Huskies 2019");
    group.addModerator(user);
    assertTrue(GroupService.deleteGroup(user, "Huskies 2019"));
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
  }

  @Test
  void deleteGroupUnsuccessful() {
    User user = UserService.findUser("neha");
    assertTrue(GroupService.createGroup("Huskies 2019", true, user));
    assertFalse(GroupService.deleteGroup(user, "Huskies 2019 2"));
  }

  @Test
  void deleteGroupParticipantSuccessful() {
    User user = UserService.findUser("neha");
    GroupService.createGroup("Huskies 2019", true, user);
    Group group = GroupService.findGroup("Huskies 2019");
    User newUser = UserService.findUser("jake123");
    assertNotNull(group);
    group.addParticipants(newUser);
    assertNotNull(newUser);
    assertTrue(GroupService.deleteParticipant(group, newUser));
  }

  @Test
  void deleteGroupParticipantUnSuccessful() {
    User user = UserService.findUser("neha");
    GroupService.createGroup("Huskies 2019", true, user);
    assertFalse(GroupService.deleteParticipant(GroupService.findGroup("Huskies 2019"), UserService.findUser("abc")));
  }
  @Test
  void deleteGroupParticipantNoUserPresent() {
    User user = UserService.findUser("neha");
    GroupService.createGroup("Huskies 2019", true, user);
    Group group = GroupService.findGroup("Huskies 2019");
    assertNotNull(group);
    User newUser = UserService.findUser("jake123");
    assertFalse(GroupService.deleteParticipant(GroupService.findGroup("Huskies 2019"), newUser));
  }

  @Test
  void getGroupProfile() {
    User user = UserService.findUser("neha");
    GroupService.createGroup("Huskies 2019", true, user);
    Group group = GroupService.findGroup("Huskies 2019");
    assertEquals(GroupService.getGroupProfile(group.getGroupname()), "Group name: Huskies 2019 {Users: neha}");
  }

  @Test
  void editGroupName() {
    User user = UserService.findUser("neha");
    GroupService.createGroup("Huskies 2019", true, user);
    Group group = GroupService.findGroup("Huskies 2019");
    assertTrue(GroupService.editGroupName(group, "Huskies 2019 edit"));
  }
}
