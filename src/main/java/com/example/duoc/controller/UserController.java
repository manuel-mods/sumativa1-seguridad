package com.example.duoc.controller;

import com.example.duoc.model.User;
import com.example.duoc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Login page endpoint
     * @return the login view name
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Logout success endpoint
     * @return redirect to home page with a logout parameter
     */
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/?logout=true";
    }

    /**
     * Get all users endpoint
     * Only accessible to authenticated users
     * @param model the Model object
     * @return the users view name
     */
    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        
        // Remove passwords from the response for security
        users.forEach(user -> user.setPassword(null));
        
        model.addAttribute("users", users);
        return "users";
    }

    /**
     * Get current user details endpoint
     * Only accessible to authenticated users
     * @return the current authenticated user
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String getCurrentUser(Model model) {
        User currentUser = userService.getCurrentUser();
        
        // Remove password from the response for security
        currentUser.setPassword(null);
        
        model.addAttribute("user", currentUser);
        return "user-profile";
    }
}
