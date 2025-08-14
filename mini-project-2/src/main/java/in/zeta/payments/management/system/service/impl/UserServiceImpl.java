package in.zeta.payments.management.system.service.impl;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.exception.AuthenticationFailedException;
import in.zeta.payments.management.system.exception.UserAlreadyExistsException;
import in.zeta.payments.management.system.exception.UserNotFoundException;
import in.zeta.payments.management.system.mapper.UserResponseMapper;
import in.zeta.payments.management.system.repository.UserRepository;
import in.zeta.payments.management.system.security.CustomUserDetails;
import in.zeta.payments.management.system.security.JwtTokenProvider;
import in.zeta.payments.management.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static in.zeta.payments.management.system.mapper.UserResponseMapper.mapToUserResponse;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder =new BCryptPasswordEncoder(10);
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public UserResponse createUser(User user) {
        String email = user.getEmail();
        logger.info("Attempting to create user with email: {}", email);
        boolean userExists = userRepository.existsByEmail(email);
        if (userExists) {
            logger.warn("User with email {} already exists", email);
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getUserID());
        return mapToUserResponse(savedUser);
    }

    @Override
    public String verifyUserCredentials(String email, String password) {
        logger.info("Verifying credentials for email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !encoder.matches(password, user.get().getPassword())) {
            logger.error("Authentication failed for email: {}", email);
            throw new AuthenticationFailedException("Invalid email or password");
        }
        logger.info("Authentication successful for email: {}", email);
        Authentication authentication = authenticateUser(user.get());
        return jwtTokenProvider.generateToken(authentication);
    }


    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Changing password for user ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        if (!encoder.matches(currentPassword, existingUser.getPassword())) {
            logger.error("Current password is incorrect for user ID: {}", userId);
            throw new AuthenticationFailedException("Current password is incorrect");
        }

        existingUser.setPassword(encoder.encode(newPassword));
        userRepository.save(existingUser);
        logger.info("Password changed successfully for user ID: {}", userId);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(UserResponseMapper::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserByID(Long userID) {
        logger.info("Fetching user by ID: {}", userID);
        return userRepository.findById(userID)
                .map(UserResponseMapper::mapToUserResponse)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userID);
                    return new UserNotFoundException("User not found with ID: " + userID);
                });
    }

    @Override
    public UserResponse updateUser(Long userID, User user) {
        logger.info("Updating user with ID: {}", userID);
        User existingUser = userRepository.findById(userID)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userID);
                    return new UserNotFoundException("User not found with ID: " + userID);
                });

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        User updatedUser = userRepository.save(existingUser);
        logger.info("User updated successfully with ID: {}", userID);
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userID) {
        logger.info("Deleting user with ID: {}", userID);
        boolean userExists = userRepository.existsById(userID);
       if(!userExists) {
              logger.error("User not found with ID: {}", userID);
              throw new UserNotFoundException("User not found with ID: " + userID);
       }
        userRepository.deleteById(userID);
        logger.info("User deleted successfully with ID: {}", userID);
    }

    private Authentication authenticateUser(User user) {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
    }

}
