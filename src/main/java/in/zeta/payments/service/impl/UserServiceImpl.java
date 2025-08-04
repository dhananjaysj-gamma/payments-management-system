package in.zeta.payments.service.impl;

import in.zeta.payments.dao.impl.UserDAOImpl;
import in.zeta.payments.dto.UserDTO;
import in.zeta.payments.exceptions.UserNotFoundException;
import in.zeta.payments.models.User;
import in.zeta.payments.service.UserService;
import in.zeta.payments.util.EncryptionUtil;

public class UserServiceImpl implements UserService {
    private final UserDAOImpl userDAO;

    public UserServiceImpl(UserDAOImpl userDAO) {
        this.userDAO=userDAO;
    }

    @Override
    public int login(String email, String password) {
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
           throw new UserNotFoundException("Please provide valid email");
        }
        String decryptedPassword = EncryptionUtil.decrypt(user.getPassword());
        if (decryptedPassword.equals(password)) {
            System.out.println("Login Successful");;
        } else {
            System.out.println("Login failed, Please enter correct password");
        }
        return user.getUserId();
    }

    @Override
    public UserDTO registerUser(User user) {
        user.setName(user.getUserName());
        user.setEmail(user.getEmail());
        String encryptedPassword = EncryptionUtil.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);

        userDAO.saveUser(user);
        return new UserDTO(user.getUserId(), user.getUserName());
    }

    @Override
    public UserDTO getUserById(int userId) {
        User user = userDAO.findUserById(userId);
        return new UserDTO(user.getUserId(), user.getUserName());
    }

}
