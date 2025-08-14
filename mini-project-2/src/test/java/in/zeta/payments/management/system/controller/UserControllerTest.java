package in.zeta.payments.management.system.controller;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.factory.UserFactory;
import in.zeta.payments.management.system.request.PasswordChangeRequest;
import in.zeta.payments.management.system.request.UserRequest;
import in.zeta.payments.management.system.security.CustomUserDetails;
import in.zeta.payments.management.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static in.zeta.payments.management.system.factory.UserFactory.USER_ID;
import static in.zeta.payments.management.system.factory.UserFactory.getUser;
import  static in.zeta.payments.management.system.mapper.UserResponseMapper.mapToUserResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = getUser();
        UserResponse userResponse = mapToUserResponse(user);
        when(userService.createUser(user)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.createUser(user);

        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testGetAllUsers() {
        User user = getUser();
        UserResponse userResponse = mapToUserResponse(user);
        List<UserResponse> users = Arrays.asList(userResponse);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        User user = getUser();
        UserResponse userResponse = mapToUserResponse(user);
        when(userService.getUserByID(USER_ID)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserById(USER_ID);

        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).getUserByID(USER_ID);
    }

    @Test
    void testUpdateUser() {
        User user = getUser();
        UserResponse userResponse = mapToUserResponse(user);
        when(userService.updateUser(USER_ID, user)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.updateUser(USER_ID, user);

        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).updateUser(USER_ID, user);
    }

    @Test
    void testDeleteUser() {

        ResponseEntity<Void> response = userController.deleteUser(USER_ID);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(USER_ID);
    }

    @Test
    void testLogin() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");
        String token = "jwt-token";
        when(userService.verifyUserCredentials(userRequest.getEmail(), userRequest.getPassword())).thenReturn(token);

        ResponseEntity<String> response = userController.login(userRequest);

        assertEquals(token, response.getBody());
        verify(userService, times(1)).verifyUserCredentials(userRequest.getEmail(), userRequest.getPassword());
    }

    @Test
    void testGetMyProfile() {
        User user = getUser();
        CustomUserDetails principal = new CustomUserDetails(user);
        user.setUserID(USER_ID);
        UserResponse userResponse = new UserResponse();
        when(userService.getUserByID(principal.getUserID())).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getMyProfile(principal);

        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).getUserByID(principal.getUserID());
    }

    @Test
    void testChangePassword() {
        User user = UserFactory.getUser();
        CustomUserDetails principal = new CustomUserDetails(user);
        user.setUserID(USER_ID);
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        ResponseEntity<String> response = userController.changePassword(principal, request);

        assertEquals("Password updated successfully", response.getBody());
        verify(userService, times(1)).changePassword(principal.getUserID(), request.getCurrentPassword(), request.getNewPassword());
    }
}