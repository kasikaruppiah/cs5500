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

package edu.northeastern.ccs.im.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The statistics for operations.
 */
@Entity
@Table(name = "COUNT_ACTIVE_USERS")
public class NumberOfActiveUsers implements Serializable {
  /**
   * Primary identifier of a group
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Name of the group
   */
  @Column(nullable = false)
  private int count;


  /**
   * The timestamp when the statistic was logged
   */
  @Basic(optional = false)
  @Column(updatable = false)
  private Timestamp timeStamp;

  /**
   * Instantiates a new Number of active users.
   */
  public NumberOfActiveUsers() {
  }

  /**
   * Instantiates a new Number of active users.
   *
   * @param timeStamp the time stamp
   * @param count     the count
   */
  public NumberOfActiveUsers(Timestamp timeStamp, int count) {
    this.count = count;
    this.timeStamp = timeStamp;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  public int getCount() {
    return count;
  }

  /**
   * Sets count.
   *
   * @param count the count
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * Gets time stamp.
   *
   * @return the time stamp
   */
  public Timestamp getTimeStamp() {
    return timeStamp;
  }

  /**
   * Sets time stamp.
   *
   * @param timeStamp the time stamp
   */
  public void setTimeStamp(Timestamp timeStamp) {
    this.timeStamp = timeStamp;
  }
}
