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

import edu.northeastern.ccs.im.services.EntityManagerResource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EntityManagerHelper {

  private static EntityManager em = EntityManagerResource.getEntityManager();

  private void EntityManagerResource() {

  }

  public static void truncateAll() {
    truncateUsers();
    truncateMessages();
    truncateGroups();
    truncateGroupParticipation();
    truncateGroupUnseenTemporary();
  }

  public static void truncateUsers() {
    em.getTransaction().begin();

    Query query = em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0");
    query.executeUpdate();

    query = em.createNativeQuery("TRUNCATE TABLE USER_TABLE");
    query.executeUpdate();

    query = em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1");
    query.executeUpdate();

    em.getTransaction().commit();
  }

  public static void truncateMessages() {
    em.getTransaction().begin();

    Query query = em.createNativeQuery("TRUNCATE TABLE MESSAGE_TABLE");
    query.executeUpdate();

    em.getTransaction().commit();
  }

  public static void truncateGroups() {
    em.getTransaction().begin();

    Query query = em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0");
    query.executeUpdate();

    query = em.createNativeQuery("TRUNCATE TABLE GROUP_TABLE");
    query.executeUpdate();

    query = em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1");
    query.executeUpdate();

    em.getTransaction().commit();
  }

  public static void truncateGroupParticipation() {
    em.getTransaction().begin();

    Query query = em.createNativeQuery("TRUNCATE TABLE GROUP_PARTICIPATION");
    query.executeUpdate();

    em.getTransaction().commit();
  }

  public static void truncateGroupUnseenTemporary() {
    em.getTransaction().begin();

    Query query = em.createNativeQuery("TRUNCATE TABLE GROUP_UNSEEN_TEMPORARY_TABLE");
    query.executeUpdate();

    em.getTransaction().commit();
  }
}
