package com.example.duoc.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.duoc.model.User;
import com.example.duoc.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  private User testUser;
  private User adminUser;
  private List<User> testUsers;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");
    testUser.setPassword("password123");
    testUser.setRoles(Set.of("ROLE_USER"));

    adminUser = new User();
    adminUser.setId(2L);
    adminUser.setUsername("admin");
    adminUser.setEmail("admin@example.com");
    adminUser.setPassword("admin123");
    adminUser.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

    testUsers = Arrays.asList(testUser, adminUser);

    when(userService.findAllUsers()).thenReturn(testUsers);
    when(userService.getCurrentUser()).thenReturn(testUser);
  }

  @Test
  void login_ReturnsLoginView() throws Exception {
    mockMvc
        .perform(get("/login"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("login"));
  }

  @Test
  void logoutSuccess_RedirectsToHomeWithLogoutParam() throws Exception {
    mockMvc
        .perform(get("/logout-success"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/?logout=true"));
  }

  @Test
  void getAllUsers_WhenAuthenticated_ShowsAllUsers() throws Exception {
    mockMvc
        .perform(get("/users").with(user("testuser").password("password123").roles("USER")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("users"))
        .andExpect(view().name("users"));

    verify(userService).findAllUsers();
  }

  @Test
  void getAllUsers_WhenNotAuthenticated_RedirectsToLogin() throws Exception {
    mockMvc
        .perform(get("/users"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }

  @Test
  void getCurrentUser_WhenAuthenticated_ShowsUserProfile() throws Exception {
    mockMvc
        .perform(get("/me").with(user("testuser").password("password123").roles("USER")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("user"))
        .andExpect(view().name("user-profile"));

    verify(userService).getCurrentUser();
  }

  @Test
  void getCurrentUser_WhenNotAuthenticated_RedirectsToLogin() throws Exception {
    mockMvc
        .perform(get("/me"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }
}
