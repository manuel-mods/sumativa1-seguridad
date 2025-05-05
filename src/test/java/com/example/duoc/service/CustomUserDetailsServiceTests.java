package com.example.duoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.duoc.model.User;
import com.example.duoc.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {

  @Mock private UserRepository userRepository;

  @InjectMocks private CustomUserDetailsService customUserDetailsService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");
    testUser.setPassword("encodedPassword");
    testUser.setRoles(Set.of("ROLE_USER"));
  }

  @Test
  void loadUserByUsername_WithExistingUsername_ReturnsUserDetails() {
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

    assertNotNull(userDetails);
    assertEquals("testuser", userDetails.getUsername());
    assertEquals("encodedPassword", userDetails.getPassword());

    Set<String> authorities =
        userDetails.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());

    assertTrue(authorities.contains("ROLE_USER"));
    assertEquals(1, authorities.size());

    verify(userRepository).findByUsername("testuser");
  }

  @Test
  void loadUserByUsername_WithNonExistingUsername_ThrowsException() {
    String nonExistingUsername = "nonexistinguser";
    when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

    Exception exception =
        assertThrows(
            UsernameNotFoundException.class,
            () -> {
              customUserDetailsService.loadUserByUsername(nonExistingUsername);
            });

    assertTrue(
        exception.getMessage().contains("User not found with username: " + nonExistingUsername));
    verify(userRepository).findByUsername(nonExistingUsername);
  }

  @Test
  void loadUserByUsername_WithRolesWithoutPrefix_AddsRolePrefix() {
    User userWithRoleWithoutPrefix = new User();
    userWithRoleWithoutPrefix.setId(2L);
    userWithRoleWithoutPrefix.setUsername("roleuser");
    userWithRoleWithoutPrefix.setEmail("role@example.com");
    userWithRoleWithoutPrefix.setPassword("password");
    userWithRoleWithoutPrefix.setRoles(Set.of("USER", "ADMIN")); // Roles without ROLE_ prefix

    when(userRepository.findByUsername("roleuser"))
        .thenReturn(Optional.of(userWithRoleWithoutPrefix));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("roleuser");

    Set<String> authorities =
        userDetails.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());

    assertTrue(authorities.contains("ROLE_USER"));
    assertTrue(authorities.contains("ROLE_ADMIN"));
    assertEquals(2, authorities.size());

    verify(userRepository).findByUsername("roleuser");
  }
}
