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

package edu.northeastern.ccs.im.message;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.GroupService;
import edu.northeastern.ccs.im.services.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for message for deleting a group.
 */
class DeleteGroupMessageTest {
  @BeforeEach
  void setup() {
    UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
    User user = UserService.findUser("neha");
    GroupService.createGroup("testGroup", true, user);
  }

  @AfterEach
  void truncateTable() {
    EntityManagerHelper.truncateUsers();
    EntityManagerHelper.truncateGroups();
  }

  @Test
  void testDispatch() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getName()).thenReturn("neha");
    Message message = MessageFactory.makeDeleteGroupMessage("neha", "testGroup");
    assertTrue(message.dispatch(clientRunnable));
  }
}
