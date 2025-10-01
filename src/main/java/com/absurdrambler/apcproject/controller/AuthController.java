package com.absurdrambler.apcproject.controller;

import com.absurdrambler.apcproject.dto.LoginRequest;
import com.absurdrambler.apcproject.dto.LoginResponse;
import com.absurdrambler.apcproject.entity.User;
import com.absurdrambler.apcproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for authentication and user management endpoints
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Get current user information
     */
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(new LoginResponse(true, "User authenticated", username, role));
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Not authenticated", null, null));
    }
}

/**
 * Controller for user management (admin only)
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    /**
     * Get all users (admin only)
     */
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserInfo> userInfos = users.stream()
                .map(user -> new UserInfo(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userInfos);
    }

    /**
     * Inner class for user information response
     */
    public static class UserInfo {
        public Long id;
        public String username;

        public UserInfo(Long id, String username) {
            this.id = id;
            this.username = username;
        }
    }
}
