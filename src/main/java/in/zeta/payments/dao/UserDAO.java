package in.zeta.payments.dao;

import in.zeta.payments.models.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User findUserById(int userId);
    User findUserByEmail(String email);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int userId);
}
