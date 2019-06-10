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

package edu.northeastern.ccs.im.client;

import edu.northeastern.ccs.im.client.command.AddGroupCommand;
import edu.northeastern.ccs.im.client.command.AddParticipantGroupCommand;
import edu.northeastern.ccs.im.client.command.BroadcastMessageCommand;
import edu.northeastern.ccs.im.client.command.CommandInterface;
import edu.northeastern.ccs.im.client.command.DeleteGroupCommand;
import edu.northeastern.ccs.im.client.command.DeleteParticipantGroupCommand;
import edu.northeastern.ccs.im.client.command.EditCommand;
import edu.northeastern.ccs.im.client.command.EditGroupApprovalCommand;
import edu.northeastern.ccs.im.client.command.GroupInviteCommand;
import edu.northeastern.ccs.im.client.command.GroupMessageCommand;
import edu.northeastern.ccs.im.client.command.HelpCommand;
import edu.northeastern.ccs.im.client.command.LogoutCommand;
import edu.northeastern.ccs.im.client.command.NotificationMessageCommand;
import edu.northeastern.ccs.im.client.command.OpenCommand;
import edu.northeastern.ccs.im.client.command.PrivateMessageCommand;
import edu.northeastern.ccs.im.client.command.ProfileCommand;
import edu.northeastern.ccs.im.client.command.SearchCommand;
import edu.northeastern.ccs.im.client.command.TagCommand;
import edu.northeastern.ccs.im.client.command.UnTagCommand;

public class CommandDecoder {

  private CommandInterface command;

  public CommandDecoder(String command) {
    String[] tokens = command.split(" ");
    if (tokens.length > 0) {
      String firstToken = tokens[0];
      if (firstToken.startsWith(Symbols.USER_SYMBOL.toString())) {
        handleUser(tokens);
      } else if (firstToken.startsWith(Symbols.GROUP_SYMBOL.toString())) {
        handleGroup(tokens);
      } else if (firstToken.startsWith(Symbols.OPTIONS_SYMBOL.toString())) {
        handleOption(tokens);
      } else if (firstToken.equals(Symbols.BROADCAST_SYMBOL.toString())) {
        this.command = new BroadcastMessageCommand(tokens);
      } else {
        this.command = new HelpCommand(tokens);
      }
    }
  }

  private void handleOption(String[] tokens) {
    switch (tokens[0]) {
      case "/quit":
        command = new LogoutCommand();
        break;
      case "/help":
        command = new HelpCommand(new String[0]);
        break;
      case "/get":
        command = new NotificationMessageCommand();
        break;
      case "/profile":
        command = new ProfileCommand(tokens);
        break;
      case "/open":
        if (tokens.length != 2 || !(tokens[1].startsWith("$") || tokens[1].startsWith("@"))) {
          command = new HelpCommand(tokens);
        } else {
          command = new OpenCommand(tokens);
        }
        break;
      case "/edit":
        if (tokens.length != 3) {
          command = new HelpCommand(tokens);
        } else {
          command = new EditCommand(tokens);
        }
        break;
      case "/search":
        if (tokens.length != 2 &&
            (tokens[1].startsWith("$") || tokens[1].equalsIgnoreCase("@"))) {
          command = new HelpCommand(tokens);
        } else {
          command = new SearchCommand(tokens);
        }
        break;
      case "/tag":
        if (tokens.length != 2) {
          command = new HelpCommand(tokens);
        } else {
          command = new TagCommand(tokens);
        }
        break;
      case "/untag":
        if (tokens.length != 2) {
          command = new HelpCommand(tokens);
        } else {
          command = new UnTagCommand(tokens);
        }
        break;
      default:
        command = new HelpCommand(tokens);
        break;
    }
  }

  private void handleUser(String[] tokens) {
    //decode the user id and then do other stuff
    command = new PrivateMessageCommand(tokens);
  }

  private void handleGroup(String[] tokens) {
    //decode the group id and do the other stuff
    switch (tokens[0]) {
      case "@new":
        command = new AddGroupCommand(tokens);
        break;
      case "@delete":
        command = new DeleteGroupCommand(tokens);
        break;
      case "@add":
        command = new AddParticipantGroupCommand(tokens);
        break;
      case "@deleteParticipant":
        command = new DeleteParticipantGroupCommand(tokens);
        break;
      case "@editapproval":
        if (tokens.length != 2) {
          command = new HelpCommand(tokens);
        } else {
          command = new EditGroupApprovalCommand(tokens);
        }
        break;
      case "@invite":
        command = new GroupInviteCommand(tokens);
        break;
      default:
        command = new GroupMessageCommand(tokens);
        break;
    }
  }


  public CommandInterface getCommand() {
    return command;
  }
  //Maybe build a method for every
}
