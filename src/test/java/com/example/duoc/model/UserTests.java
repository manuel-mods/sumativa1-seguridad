package com.example.duoc.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTests {

  private User user;
  private final String username = "testuser";
  private final String email = "test@example.com";
  private final String password = "password123";

  @BeforeEach
  void setUp() {
    user = new User(username, email, password);
  }

  @Test
  void testNoArgsConstructor() {
    User emptyUser = new User();
    assertNotNull(emptyUser);
    assertNull(emptyUser.getId());
    assertNull(emptyUser.getUsername());
    assertNull(emptyUser.getEmail());
    assertNull(emptyUser.getPassword());
    assertNotNull(emptyUser.getRoles());
    assertTrue(emptyUser.getRoles().isEmpty());
  }

  @Test
  void testAllArgsConstructor() {
    assertEquals(username, user.getUsername());
    assertEquals(email, user.getEmail());
    assertEquals(password, user.getPassword());
    assertNotNull(user.getRoles());
    assertTrue(user.getRoles().isEmpty());
  }

  @Test
  void testIdGetterAndSetter() {
    User testUser = new User();
    Long id = 1L;
    testUser.setId(id);
    assertEquals(id, testUser.getId());
  }

  @Test
  void testUsernameGetterAndSetter() {
    User testUser = new User();
    testUser.setUsername("newUsername");
    assertEquals("newUsername", testUser.getUsername());
  }

  @Test
  void testEmailGetterAndSetter() {
    User testUser = new User();
    testUser.setEmail("new@example.com");
    assertEquals("new@example.com", testUser.getEmail());
  }

  @Test
  void testPasswordGetterAndSetter() {
    User testUser = new User();
    testUser.setPassword("newPassword");
    assertEquals("newPassword", testUser.getPassword());
  }

  @Test
  void testRolesGetterAndSetter() {
    User testUser = new User();
    Set<String> roles = new HashSet<>();
    roles.add("ROLE_USER");
    roles.add("ROLE_ADMIN");

    testUser.setRoles(roles);

    assertEquals(roles, testUser.getRoles());
    assertEquals(2, testUser.getRoles().size());
    assertTrue(testUser.getRoles().contains("ROLE_USER"));
    assertTrue(testUser.getRoles().contains("ROLE_ADMIN"));
  }

  @Test
  void testAddRole() {
    User testUser = new User();

    assertTrue(testUser.getRoles().isEmpty());

    testUser.addRole("ROLE_USER");

    assertEquals(1, testUser.getRoles().size());
    assertTrue(testUser.getRoles().contains("ROLE_USER"));

    testUser.addRole("ROLE_ADMIN");

    assertEquals(2, testUser.getRoles().size());
    assertTrue(testUser.getRoles().contains("ROLE_USER"));
    assertTrue(testUser.getRoles().contains("ROLE_ADMIN"));

    testUser.addRole("ROLE_USER");

    assertEquals(2, testUser.getRoles().size());
  }
}
