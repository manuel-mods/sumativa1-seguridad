package com.example.duoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.duoc.model.User;
import com.example.duoc.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @InjectMocks private UserService userService;

  private User testUser;
  private User adminUser;
  private List<User> userList;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");
    testUser.setPassword("rawPassword");
    testUser.setRoles(Set.of("ROLE_USER"));

    adminUser = new User();
    adminUser.setId(2L);
    adminUser.setUsername("admin");
    adminUser.setEmail("admin@example.com");
    adminUser.setPassword("adminPassword");
    adminUser.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

    userList = Arrays.asList(testUser, adminUser);
  }

  @Test
  void findAllUsers_ReturnsAllUsers() {
    when(userRepository.findAll()).thenReturn(userList);

    List<User> result = userService.findAllUsers();

    assertEquals(2, result.size());
    assertEquals(testUser.getUsername(), result.get(0).getUsername());
    assertEquals(adminUser.getUsername(), result.get(1).getUsername());
    verify(userRepository).findAll();
  }

  @Test
  void findUserById_WithExistingId_ReturnsUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

    Optional<User> result = userService.findUserById(1L);

    assertTrue(result.isPresent());
    assertEquals(testUser.getUsername(), result.get().getUsername());
    verify(userRepository).findById(1L);
  }

  @Test
  void findUserById_WithNonExistingId_ReturnsEmptyOptional() {
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<User> result = userService.findUserById(999L);

    assertFalse(result.isPresent());
    verify(userRepository).findById(999L);
  }

  @Test
  void findUserByUsername_WithExistingUsername_ReturnsUser() {
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

    Optional<User> result = userService.findUserByUsername("testuser");

    assertTrue(result.isPresent());
    assertEquals(testUser.getUsername(), result.get().getUsername());
    verify(userRepository).findByUsername("testuser");
  }

  @Test
  void findUserByUsername_WithNonExistingUsername_ReturnsEmptyOptional() {
    when(userRepository.findByUsername("nonexistinguser")).thenReturn(Optional.empty());

    Optional<User> result = userService.findUserByUsername("nonexistinguser");

    assertFalse(result.isPresent());
    verify(userRepository).findByUsername("nonexistinguser");
  }

  @Test
  void findUserByEmail_WithExistingEmail_ReturnsUser() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

    Optional<User> result = userService.findUserByEmail("test@example.com");

    assertTrue(result.isPresent());
    assertEquals(testUser.getEmail(), result.get().getEmail());
    verify(userRepository).findByEmail("test@example.com");
  }

  @Test
  void getCurrentUser_WhenAuthenticated_ReturnsCurrentUser() {
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testuser");
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

    User result = userService.getCurrentUser();

    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    verify(userRepository).findByUsername("testuser");

    SecurityContextHolder.clearContext();
  }

  @Test
  void getCurrentUser_WhenUserNotFound_ThrowsException() {
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("nonexistinguser");
    when(userRepository.findByUsername("nonexistinguser")).thenReturn(Optional.empty());

    Exception exception =
        assertThrows(UsernameNotFoundException.class, () -> userService.getCurrentUser());

    assertTrue(exception.getMessage().contains("User not found with username: nonexistinguser"));
    verify(userRepository).findByUsername("nonexistinguser");

    SecurityContextHolder.clearContext();
  }

  @Test
  void createUser_WithValidUser_CreatesUser() {
    User newUser = new User();
    newUser.setUsername("newuser");
    newUser.setEmail("new@example.com");
    newUser.setPassword("rawPassword");

    when(userRepository.existsByUsername("newuser")).thenReturn(false);
    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User savedUser = invocation.getArgument(0);
              savedUser.setId(3L);
              return savedUser;
            });

    User result = userService.createUser(newUser);

    assertNotNull(result);
    assertEquals(3L, result.getId());
    assertEquals("newuser", result.getUsername());
    assertEquals("encodedPassword", result.getPassword());

    assertEquals(1, result.getRoles().size());
    assertTrue(result.getRoles().contains("ROLE_USER"));

    verify(userRepository).existsByUsername("newuser");
    verify(userRepository).existsByEmail("new@example.com");
    verify(passwordEncoder).encode("rawPassword");
    verify(userRepository).save(newUser);
  }

  @Test
  void createUser_WithExistingUsername_ThrowsException() {
    User newUser = new User();
    newUser.setUsername("testuser");
    newUser.setEmail("new@example.com");
    newUser.setPassword("rawPassword");

    when(userRepository.existsByUsername("testuser")).thenReturn(true);

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.createUser(newUser);
            });

    assertEquals("Username already exists", exception.getMessage());
    verify(userRepository).existsByUsername("testuser");
    verifyNoMoreInteractions(userRepository, passwordEncoder);
  }

  @Test
  void createUser_WithExistingEmail_ThrowsException() {
    User newUser = new User();
    newUser.setUsername("newuser");
    newUser.setEmail("test@example.com");
    newUser.setPassword("rawPassword");

    when(userRepository.existsByUsername("newuser")).thenReturn(false);
    when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.createUser(newUser);
            });

    assertEquals("Email already exists", exception.getMessage());
    verify(userRepository).existsByUsername("newuser");
    verify(userRepository).existsByEmail("test@example.com");
    verifyNoMoreInteractions(userRepository, passwordEncoder);
  }

  @Test
  void updateUser_SavesUpdatedUser() {
    User updatedUser = testUser;
    updatedUser.setEmail("updated@example.com");

    when(userRepository.save(updatedUser)).thenReturn(updatedUser);

    User result = userService.updateUser(updatedUser);

    assertEquals("updated@example.com", result.getEmail());
    verify(userRepository).save(updatedUser);
  }

  @Test
  void deleteUser_DeletesUserById() {

    userService.deleteUser(1L);

    verify(userRepository).deleteById(1L);
  }
}
