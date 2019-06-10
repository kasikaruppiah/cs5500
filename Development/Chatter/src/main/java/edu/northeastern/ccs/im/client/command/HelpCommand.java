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

package edu.northeastern.ccs.im.client.command;

import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.client.LoggerClass;

public class HelpCommand implements CommandInterface {
  private String text;

  public HelpCommand(String[] tokens) {
    text = String.join(" ", tokens);
  }
  @Override
  public void executeCommand(IMConnection connection) {
       if (!text.isEmpty()) {
           LoggerClass.logInfo("Invalid command, " + text, this.getClass());
       }
        LoggerClass.logInfo("These are available slack commands used in various situations:", this.getClass());
        LoggerClass.logInfo("Logout => /quit", this.getClass());
        LoggerClass.logInfo("Private Message => $[userId] Message", this.getClass());
        LoggerClass.logInfo("Group Message => @[GroupId] Message", this.getClass());
        LoggerClass.logInfo("Broadcast Message => ! Message", this.getClass());
        LoggerClass.logInfo("Notifications => /get", this.getClass());
        LoggerClass.logInfo("Request Messages From a User => $[userId]", this.getClass());
        LoggerClass.logInfo("View Profile => /profile", this.getClass());
        LoggerClass.logInfo("View a users Profile => /profile $[userId]", this.getClass());
        LoggerClass.logInfo("View a groups Profile => /profile @[groupId]", this.getClass());
        LoggerClass.logInfo("To edit firstname => /edit firstName [$newFirstName]", this.getClass());
        LoggerClass.logInfo("To edit lastname => /edit lastName [$newLastName]", this.getClass());
        LoggerClass.logInfo("To edit password => /edit password [$newPasswordName]", this.getClass());
        LoggerClass.logInfo("To search a user => /search  $[$userToSearch]", this.getClass());
        LoggerClass.logInfo("To search a group => /search @[$groupToSearch]", this.getClass());
        LoggerClass.logInfo("To create a group => @new [groupname] [approval(yes/no)]", this.getClass());
        LoggerClass.logInfo("To delete a group => @delete [groupname]", this.getClass());
   
  }
}
