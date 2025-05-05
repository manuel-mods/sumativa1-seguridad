package com.example.duoc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomErrorControllerTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void handleError_WithNotFoundStatus_ReturnsNotFoundView() throws Exception {
    MockHttpServletRequestBuilder requestBuilder = get("/error")
        .with(user("user1").password("password1").roles("USER"))
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
  }

  @Test
  void handleError_WithForbiddenStatus_ReturnsForbiddenView() throws Exception {
    MockHttpServletRequestBuilder requestBuilder = get("/error")
        .with(user("user1").password("password1").roles("USER"))
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value());

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
  }

  @Test
  void handleError_WithInternalServerErrorStatus_ReturnsServerErrorView() throws Exception {
    MockHttpServletRequestBuilder requestBuilder = get("/error")
        .with(user("user1").password("password1").roles("USER"))
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());

    mockMvc
        .perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
  }

  @Test
  void handleError_WithNoStatus_ReturnsGenericErrorView() throws Exception {
    mockMvc
        .perform(get("/error").with(user("user1").password("password1").roles("USER")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
  }
}