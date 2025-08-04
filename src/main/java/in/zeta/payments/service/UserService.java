package in.zeta.payments.service;

import in.zeta.payments.dto.UserDTO;
import in.zeta.payments.models.User;

public interface UserService {

    UserDTO registerUser(User user);
    int login(String email, String plainPassword);
    UserDTO getUserById(int userId);
}
