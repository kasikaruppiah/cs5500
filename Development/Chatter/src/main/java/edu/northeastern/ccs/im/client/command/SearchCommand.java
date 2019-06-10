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
import edu.northeastern.ccs.im.client.Symbols;
import edu.northeastern.ccs.im.client.message.Message;
import edu.northeastern.ccs.im.client.message.MessageFactory;

/**
 * Command executed when a search command is called by the user. You can either search for a user or
 * a group.
 */
public class SearchCommand implements CommandInterface {
  private String parameter;
  private String query;

  public SearchCommand(String[] tokens) {
    if(tokens[1].startsWith(Symbols.GROUP_SYMBOL.toString())){

      this.parameter = "group";
    }
    else if(tokens[1].startsWith(Symbols.USER_SYMBOL.toString())){
      this.parameter = "user";
    }
    this.query = tokens[1].substring(1);
  }

  @Override
  public void executeCommand(IMConnection connection) {
    Message search = MessageFactory.makeSearchMessage(this.parameter, this.query);
    connection.sendMessage(search);
  }
}
