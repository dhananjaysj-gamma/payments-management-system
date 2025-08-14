package in.zeta.payments.management.system.service;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.enums.Role;
import in.zeta.payments.management.system.exception.AuthenticationFailedException;
import in.zeta.payments.management.system.exception.UserAlreadyExistsException;
import in.zeta.payments.management.system.exception.UserNotFoundException;
import in.zeta.payments.management.system.repository.UserRepository;
import in.zeta.payments.management.system.security.JwtTokenProvider;
import in.zeta.payments.management.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static in.zeta.payments.management.system.factory.UserFactory.USER_ID;
import static in.zeta.payments.management.system.factory.UserFactory.getUser;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(10);
    }

    @Test
    void testCreateUser_Success() {
        User user = getUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(user);

        assertNotNull(response);
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User user = getUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerifyUserCredentials_Success() {
        User user = getUser();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setPassword(encoder.encode("password"));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(any())).thenReturn("jwt-token");

        String token = userService.verifyUserCredentials(user.getEmail(), "password");

        assertEquals("jwt-token", token);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(jwtTokenProvider, times(1)).generateToken(any());
    }

    @Test
    void testVerifyUserCredentials_InvalidCredentials() {
        User user = getUser();
        user.setEmail("test@example.com");
        user.setPassword(encoder.encode("password"));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(AuthenticationFailedException.class, () -> userService.verifyUserCredentials(user.getEmail(), "wrong-password"));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testChangePassword_Success() {
        User user = getUser();
        user.setUserID(USER_ID);
        user.setPassword(encoder.encode("oldPassword"));

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.changePassword(user.getUserID(), "oldPassword", "newPassword");

        verify(userRepository, times(1)).findById(user.getUserID());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword_UserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(USER_ID, "oldPassword", "newPassword"));
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_InvalidCurrentPassword() {
        User user = getUser();
        user.setUserID(USER_ID);
        user.setPassword(encoder.encode("oldPassword"));

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));

        assertThrows(AuthenticationFailedException.class, () -> userService.changePassword(user.getUserID(), "wrongPassword", "newPassword"));
        verify(userRepository, times(1)).findById(user.getUserID());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(getUser()));

        var users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByID_Success() {
        User user = getUser();

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserByID(user.getUserID());

        assertNotNull(response);
        verify(userRepository, times(1)).findById(user.getUserID());
    }

    @Test
    void testGetUserByID_UserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByID(USER_ID));
        verify(userRepository, times(1)).findById(USER_ID);

    }

    @Test
    void testUpdateUser_Success() {
        User user = getUser();
        user.setUsername("oldUsername");

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponse response = userService.updateUser(user.getUserID(), updatedUser);

        assertNotNull(response);
        verify(userRepository, times(1)).findById(user.getUserID());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(USER_ID, new User()));
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_UsernameOnly() {
        User user = getUser();
        user.setUsername("oldUsername");
        User update = new User();
        update.setUsername("newUsername");

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(user.getUserID(), update);

        verify(userRepository).findById(user.getUserID());
        verify(userRepository).save(argThat(u -> "newUsername".equals(u.getUsername())));
    }

    @Test
    void testUpdateUser_EmailOnly() {
        User user = getUser();
        user.setEmail("old@email.com");
        User update = new User();
        update.setEmail("new@email.com");

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(user.getUserID(), update);

        verify(userRepository).findById(user.getUserID());
        verify(userRepository).save(argThat(u -> "new@email.com".equals(u.getEmail())));
    }

    @Test
    void testUpdateUser_RoleOnly() {
        User user = getUser();
        user.setRole(Role.VIEWER);
        User update = new User();
        update.setRole(Role.ADMIN);

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(user.getUserID(), update);

        verify(userRepository).findById(user.getUserID());
        verify(userRepository).save(argThat(u -> Role.ADMIN.equals(u.getRole())));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);

        userService.deleteUser(USER_ID);


        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, times(1)).deleteById(USER_ID);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(USER_ID));
        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, never()).deleteById(USER_ID);
    }
}