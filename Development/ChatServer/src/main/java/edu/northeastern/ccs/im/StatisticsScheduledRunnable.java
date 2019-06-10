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

package edu.northeastern.ccs.im;

import java.util.Queue;

import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.StatisticsService;

/**
 * The type Statistics scheduled runnable.
 */
public class StatisticsScheduledRunnable implements Runnable {
  private Queue<ClientRunnable> active;

  /**
   * Instantiates a new Statistics scheduled runnable.
   *
   * @param active the active
   */
  public StatisticsScheduledRunnable(Queue<ClientRunnable> active) {
    this.active = active;
  }

  @Override
  public void run() {
    int count = 0;
    // Loop through all of our active threads
    for (ClientRunnable tt : active) {
      // Id active increase the count
      if (tt.isInitialized()) {
        count++;
      }
    }
    StatisticsService.addStatistic(count);
  }
}
