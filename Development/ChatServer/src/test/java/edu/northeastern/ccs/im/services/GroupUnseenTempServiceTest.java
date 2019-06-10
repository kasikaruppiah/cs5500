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

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.models.Group;
import edu.northeastern.ccs.im.models.MessageModel;
import edu.northeastern.ccs.im.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupUnseenTempServiceTest {
    @BeforeEach
    void setup() {
        UserService.createUser("jake123", "jake123##", "Jake", "Dan", "jd@gmail.com");
        UserService.createUser("neha", "neha", "Neha", "Gundecha", "ng@gmail.com");
        GroupService.createGroup("MSD",false,UserService.findUser("neha"));
        Group group=GroupService.findGroup("MSD");
        GroupService.addParticipant(group,UserService.findUser("jake123"));
    }

    @AfterEach
    void truncateTable() {
        EntityManagerHelper.truncateAll();
    }

    @Test
    public void sendGroupMessageTest(){
        User neha = UserService.findUser("neha");
        Group group = GroupService.findGroup("MSD");
        MessageModel model=MessageService.createGroupMessage(neha,group,"Hello","GRP");
        assertNotNull(GroupUnseenTemporaryService.createUnseenGroupMessage(group.getId(),neha.getId(),model.getId()));
    }
}
