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

import edu.northeastern.ccs.im.client.command.LogoutCommand;
import edu.northeastern.ccs.im.client.message.Message;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class which can be used as a command-line IM client.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International
 * License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/. It
 * is based on work originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class CommandLineMain {

  /**
   * This main method will perform all of the necessary actions for this phase of the course
   * project.
   *
   * @param args Command-line arguments which we ignore
   */
  public static void main(String[] args) {
    LoggerClass.logInfo("slack!", CommandLineMain.class);

    IMConnection connect;
    @SuppressWarnings("resource")
    Scanner in = new Scanner(System.in);

    do {
      // Create a Connection to the IM server.
      connect = new IMConnection(ConnectionArguments.url, ConnectionArguments.port);
    } while (!connect.connect());

    // Create the objects needed to read & write IM messages.
    KeyboardScanner scan = connect.getKeyboardScanner();
    MessageScanner mess = connect.getMessageScanner();

    String input = "";

    while (!Objects.equals(input, "1") && !Objects.equals(input, "2")) {
      LoggerClass.logInfo("1. Login", CommandLineMain.class);
      LoggerClass.logInfo("2. Register", CommandLineMain.class);
      input = getOption(scan);
    }

    if (input.equals("1")) {
      login(connect, scan);
    } else if (input.equals("2")) {
      register(connect, scan);
    }

    while (connect.connectionActive()) {
      if (mess.hasNext()) {
        Message message = mess.next();
        if (message.isAcknowledge()) {
          LoggerClass.logInfo("Welcome, " + connect.getUserName(), CommandLineMain.class);
          break;
        } else if (message.isNoAcknowledge()) {
          if (Objects.equals(input, "1")) {
            LoggerClass.logInfo("Sorry, you entered an incorrect username or password.",
                CommandLineMain.class);

            login(connect, scan);
          } else if (Objects.equals(input, "2")) {
            LoggerClass.logInfo("Username is already taken, please try another username.",
                CommandLineMain.class);

            register(connect, scan);
          }
        } else {
          // DO NOTHING: SKIP OTHER MESSAGES
        }
      }
    }

    scan.empty();

    // Repeat the following loop
    while (connect.connectionActive()) {
      // Check if the user has typed in a line of text to broadcast to the IM server.
      // If there is a line of text to be
      // broadcast:
      if (scan.hasNext()) {
        // Read in the text they typed
        String line = scan.nextLine();
        CommandDecoder decodedCommand = new CommandDecoder(line);
        decodedCommand.getCommand().executeCommand(connect);

        if (decodedCommand.getCommand() instanceof LogoutCommand) {
          break;
        }
      }
      // Get any recent messages received from the IM server.
      if (mess.hasNext()) {
        Message message = mess.next();

        if (!message.getSender().equals(connect.getUserName())) {
          message.print();
        }
      }
    }

    LoggerClass.logInfo("Program complete.", CommandLineMain.class);
    System.exit(0);
  }

  /**
   * Get username and password from the user and try to login on the connection
   *
   * @param connection connection object
   * @param scanner keyboard scanner
   */
  private static void login(IMConnection connection, KeyboardScanner scanner) {
    LoggerClass.logInfo("Sign in to your workspace", CommandLineMain.class);

    scanner.empty();

    String username = getUsername(scanner);
    String password = getPassword(scanner);

    connection.login(username, password);
  }

  private static void register(IMConnection connection, KeyboardScanner scanner) {
    LoggerClass.logInfo("Registration", CommandLineMain.class);

    scanner.empty();

    String username = getUsername(scanner);
    String password = getPassword(scanner);

    connection.register(username, password);
  }

  /**
   * Get a valid option for login or register
   *
   * @param scanner keyboard scanner
   * @return string username
   */
  private static String getOption(KeyboardScanner scanner) {
    String option = "";
    boolean flag = true;

    // Prompt the user to type in a username.
    LoggerClass.logInfo("Option: ", CommandLineMain.class);

    do {
      if (scanner.hasNext()) {
        option = scanner.nextLine().trim();

        flag = option.isEmpty();

        if (flag) {
          LoggerClass.logInfo("Please enter a valid option", CommandLineMain.class);
        } else {
          for (char ch : option.toCharArray()) {
            if (!(Character.isLetter(ch) || Character.isDigit(ch))) {
              LoggerClass.logInfo("Invalid option: " + option + "\nPlease try again.",
                  CommandLineMain.class);
              option = null;
              break;
            }
          }
        }
      }
    } while (flag);

    return option;
  }

  /**
   * Get a valid username from the user
   *
   * @param scanner keyboard scanner
   * @return string username
   */
  private static String getUsername(KeyboardScanner scanner) {
    String username = "";
    Boolean flag = true;

    // Prompt the user to type in a username.
    LoggerClass.logInfo("Username: ", CommandLineMain.class);

    do {
      if (scanner.hasNext()) {
        username = scanner.nextLine().trim();

        flag = username == null || username.isEmpty();

        if (flag) {
          LoggerClass.logInfo("Username can't be empty", CommandLineMain.class);
        } else {
          for (char ch : username.toCharArray()) {
            if (!(Character.isLetter(ch) || Character.isDigit(ch))) {
              LoggerClass.logInfo(
                  "Invalid username: " + username + "\nOnly Letters and Digits allowed in username",
                  CommandLineMain.class);
              username = null;
              break;
            }
          }
        }
      }
    } while (flag);

    return username;
  }

  /**
   * Get a valid password from the user
   *
   * @param scanner keyboard scanner
   * @return string password
   */
  private static String getPassword(KeyboardScanner scanner) {
    String password = "";
    Boolean flag = true;

    // Prompt the user to type in a username.
    LoggerClass.logInfo("Password: ", CommandLineMain.class);

    do {
      if (scanner.hasNext()) {
        password = scanner.nextLine().trim();

        flag = password == null || password.isEmpty();

        if (flag) {
          LoggerClass.logInfo("Password can't be empty", CommandLineMain.class);
        }
      }
    } while (flag);

    return password;
  }
}