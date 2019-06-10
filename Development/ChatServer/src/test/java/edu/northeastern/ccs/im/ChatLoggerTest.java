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

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatLoggerTest {
  @Test
  @Order(3)
  void chatLogger() throws NoSuchMethodException {
    Constructor<ChatLogger> constructor = ChatLogger.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);

    InvocationTargetException e = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertEquals(IllegalStateException.class, e.getCause().getClass());
  }

  // HandlerType is private, modified for testing
  @Test
  @Order(1)
  void setMode() {
    try {
      ChatLogger.setMode(ChatLogger.HandlerType.FILE);
      ChatLogger.warning("Test setMode with FILE");
      ChatLogger.setMode(ChatLogger.HandlerType.CONSOLE);
      ChatLogger.warning("Test setMode with CONSOLE");
      ChatLogger.setMode(ChatLogger.HandlerType.BOTH);
      ChatLogger.warning("Test setMode with Both");
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  @Order(2)
  void ioExceptionWhileThrowingFile() {
    try {
      Field path = ChatLogger.class.getDeclaredField("PATH");

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(path, path.getModifiers() & ~Modifier.FINAL);
      path.setAccessible(true);
      path.set(null, ".../$%^*");
      assertThrows(Exception.class, () -> ChatLogger.setMode(ChatLogger.HandlerType.FILE));
      path.set(null, String.format("%s/%s.log", System.getProperty("user.dir"), ChatLogger.class.getName()));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail(e.getMessage());
    }

  }
}