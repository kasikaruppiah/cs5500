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

package edu.northeastern.ccs.im.server;

import java.util.GregorianCalendar;

/**
 * Class that represents the calendar used by the ClientRunnable.
 *
 * @author Riya Nadkarni
 * @version 12-27-2018
 */
public class ClientTimer {
  /**
   * Number of milliseconds after which we terminate a client due to inactivity. This is currently
   * equal to 5 hours.
   */
  private static final long TERMINATE_AFTER_INACTIVE_BUT_LOGGEDIN_IN_MS = 18000000;

  /**
   * Number of milliseconds after which we terminate a client due to inactivity. This is currently
   * equal to 5 hours.
   */
  private static final long TERMINATE_AFTER_INACTIVE_INITIAL_IN_MS = 600000;

  /**
   * Time at which the client should be terminated due to lack of activity.
   */
  private GregorianCalendar calendar;

  /**
   * Constructor for the timer.
   */
  public ClientTimer() {
    calendar = new GregorianCalendar();
    calendar.setTimeInMillis(calendar.getTimeInMillis() + TERMINATE_AFTER_INACTIVE_INITIAL_IN_MS);
  }

  /**
   * Once the client has been initialized, updates the time until the client is terminated for
   * inactivity.
   */
  public void updateAfterInitialization() {
    calendar.setTimeInMillis(
            new GregorianCalendar().getTimeInMillis() + TERMINATE_AFTER_INACTIVE_INITIAL_IN_MS);
  }

  /**
   * Once the client receives messages, updates the time until the client is terminated for
   * inactivity.
   */
  public void updateAfterActivity() {
    calendar.setTimeInMillis(
            new GregorianCalendar().getTimeInMillis() + TERMINATE_AFTER_INACTIVE_BUT_LOGGEDIN_IN_MS);
  }

  /**
   * Checks whether the calendar represents a time before the current time.
   *
   * @return true if the time passed in is later than the current value of calendar, false
   * otherwise.
   */
  public boolean isBehind() {
    return calendar.before(new GregorianCalendar());
  }
}
