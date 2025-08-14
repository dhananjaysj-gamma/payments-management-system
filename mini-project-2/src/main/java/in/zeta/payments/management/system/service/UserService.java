package in.zeta.payments.management.system.service;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse createUser(User user);
    UserResponse getUserByID(Long userID);
    UserResponse updateUser(Long userID, @Valid User user);
    void deleteUser(Long userID);
    String verifyUserCredentials(String email, String password);
    void changePassword(Long userId, String currentPassword, String newPassword);
}
