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

import static edu.northeastern.ccs.im.services.UserService.checkUserinGroup;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * This is a test suite for user service.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

  private User neha;
  private User jake;

  @BeforeAll
  void setup() {
    UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
    UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
    neha = UserService.findUser("neha");
    jake = UserService.findUser("jake123");
    GroupService.createGroup("Huskies 2019", true, neha);
  }

  @AfterAll
  void truncateTable() {
    EntityManagerHelper.truncateAll();
  }

  @Test
  void testCheckCredentials() {
    assertTrue(UserService.checkCredentials("neha", "neha"));
  }

  @Test
  void testCheckIncorrectCredentials() {
    assertFalse(UserService.checkCredentials("whduedfh", "hsgdwue"));
  }

  @Test
  void testCheckProfile() {
    assertTrue(UserService.getProfile("neha").length() > 0);
  }

  @Test
  void testCheckRegistrationUnSuccessful() {
    assertFalse(UserService.registerUser("neha", "neha"));
  }

  @Test
  void testCheckRegistrationSuccessful() {
    assertTrue(UserService.registerUser("abc", "abc"));
  }

  @Test
  void testValidCheckUserinGroup() {
    User user = UserService.findUser("neha");
    assertTrue(checkUserinGroup(user, "Huskies 2019"));
  }

  @Test
  void testInvalidCheckUserinGroup() {
    User user = UserService.findUser("jake123");

    assertFalse(checkUserinGroup(user, "Huskies 2019"));
  }

  @Test
  void testTagUser() {
    assertTrue(neha.getFollowing().isEmpty());

    UserService.tagUser(neha, jake);

    assertFalse(neha.getFollowing().isEmpty());
    assertTrue(neha.getFollowing().contains(jake));
  }

  @Test
  void testUnTagUser() {
    UserService.tagUser(neha, jake);

    assertFalse(neha.getFollowing().isEmpty());
    assertTrue(neha.getFollowing().contains(jake));

    UserService.untagUser(neha, jake);

    assertTrue(neha.getFollowing().isEmpty());
  }
}
