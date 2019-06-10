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

import edu.northeastern.ccs.im.ChatLogger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * The hibernate entity manager. A singleton is implemented here. So that only one connection is
 * made to the database. The entity manager gives access to database and database operations.
 */
public class EntityManagerResource {

  /**
   * The name of the persistence unit.
   */
  private static final String PERSISTENCE_UNIT_NAME = "msd_PU";
  /**
   * Static reference to the Entity manager.
   */
  private static final EntityManager em = Persistence
      .createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();

  /**
   * Private constructor to prohibit creating objects of this class.
   */
  private EntityManagerResource() {
  }

  /**
   * Return the singleton entity manager for the server.
   *
   * @return the entity manager instance.
   */
  public static EntityManager getEntityManager() {
    return em;
  }

  /**
   * Persists the object provided as a parameter to the database.
   *
   * @param entity the entity to persists.
   */
  public static void persist(Object entity) {
    em.getTransaction().begin();

    try {
      em.persist(entity);

      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();

      ChatLogger.warning(e.toString());

      throw e;
    }
  }

  /**
   * Persists the list of objects in the parameter to the database.
   *
   * @param entities the list tof entities to persist.
   */
  public static void persistAll(List<?> entities) {
    em.getTransaction().begin();

    try {
      for (Object entity : entities) {
        em.persist(entity);
      }

      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();

      ChatLogger.warning(e.toString());

      throw e;
    }
  }

  /**
   * Remove the object passes as parameter from the database.
   *
   * @param entity the entity to remove.
   */
  public static void remove(Object entity) {
    em.getTransaction().begin();

    try {
      em.remove(entity);

      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();

      ChatLogger.warning(e.toString());

      throw e;
    }
  }

  /**
   * Remove the list of objects from the database.
   *
   * @param entities the list of objects to remove from the database.
   */
  public static void removeAll(List<?> entities) {
    em.getTransaction().begin();

    try {
      for (Object entity : entities) {
        em.remove(entity);
      }

      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();

      ChatLogger.warning(e.toString());

      throw e;
    }
  }
}
