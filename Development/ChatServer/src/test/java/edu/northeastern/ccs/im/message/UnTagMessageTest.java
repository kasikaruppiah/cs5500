package edu.northeastern.ccs.im.message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.northeastern.ccs.im.EntityManagerHelper;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.models.User;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnTagMessageTest {

  private String username1 = "jdoe1";
  private String username2 = "jdoe2";
  private String username3 = "jdoe3";

  private User user;

  @BeforeEach
  void setUp() {
    UserService.createUser(username1, null, null, null, null);
    UserService.createUser(username2, null, null, null, null);

    user = UserService.findUser(username1);
  }

  @AfterEach
  void tearDown() {
    EntityManagerHelper.truncateAll();
  }

  @Test
  void dispatchValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getUser()).thenReturn(user);
    when(clientRunnable.getName()).thenReturn(username2);

    assertTrue(MessageFactory.makeMessage(MessageType.TAG, username2, null).dispatch(clientRunnable));
    assertTrue(MessageFactory.makeMessage(MessageType.UNTAG, username2, null).dispatch(clientRunnable));
    assertTrue(MessageFactory.makeMessage(MessageType.UNTAG, username2, null).dispatch(clientRunnable));
  }

  @Test
  void dispatchInValid() {
    ClientRunnable clientRunnable = mock(ClientRunnable.class);
    when(clientRunnable.getUser()).thenReturn(user);
    when(clientRunnable.getName()).thenReturn(username3);

    assertTrue(MessageFactory.makeMessage(MessageType.UNTAG, username3, null).dispatch(clientRunnable));
  }
}