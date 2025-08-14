package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.request.PasswordChangeRequest;
import in.zeta.payments.management.system.request.UserRequest;
import in.zeta.payments.management.system.security.CustomUserDetails;
import in.zeta.payments.management.system.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.BASE_URL;
import static in.zeta.payments.management.system.constant.PaymentsManagementSystemRoutes.UserRoutes.*;

@RestController
@RequestMapping(value = BASE_URL)
public class UserController {

    private final UserServiceImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = CREATE_USER_URL)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        UserResponse savedUser = userService.createUser(user);
        logger.info("User created successfully with ID: {}", savedUser.getUserID());
        return ResponseEntity.ok(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = GET_ALL_USERS_URL)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("Fetching all users");
        List<UserResponse> users = userService.getAllUsers();
        logger.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = GET_USER_BY_ID_URL)
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userID) {
        logger.info("Fetching user with ID: {}", userID);
        UserResponse user = userService.getUserByID(userID);
        logger.info("User fetched successfully with ID: {}", userID);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = UPDATE_USER_URL)
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userID, @Valid @RequestBody User user) {
        logger.info("Updating user with ID: {}", userID);
        UserResponse updatedUser = userService.updateUser(userID, user);
        logger.info("User updated successfully with ID: {}", userID);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = DELETE_USER_URL)
    public ResponseEntity<Void> deleteUser(@PathVariable Long userID) {
        logger.info("Deleting user with ID: {}", userID);
        userService.deleteUser(userID);
        logger.info("User deleted successfully with ID: {}", userID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = LOGIN_URL)
    public ResponseEntity<String> login(@RequestBody UserRequest userRequest) {
        logger.info("Logging in user with email: {}", userRequest.getEmail());
        String token = userService.verifyUserCredentials(userRequest.getEmail(), userRequest.getPassword());
        logger.info("Login successful for user with email: {}", userRequest.getEmail());
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = MY_PROFILE_URL)
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails principal) {
        logger.info("Fetching profile for user ID: {}", principal.getUserID());
        UserResponse user = userService.getUserByID(principal.getUserID());
        logger.info("Profile fetched successfully for user ID: {}", principal.getUserID());
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = CHANGE_PASSWORD_URL)
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody PasswordChangeRequest request) {
        logger.info("Changing password for user ID: {}", principal.getUserID());
        userService.changePassword(principal.getUserID(), request.getCurrentPassword(), request.getNewPassword());
        logger.info("Password changed successfully for user ID: {}", principal.getUserID());
        return ResponseEntity.ok("Password updated successfully");
    }
}
