package in.zeta.payments.management.system.mapper;

import in.zeta.payments.management.system.dto.UserResponse;
import in.zeta.payments.management.system.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseMapper {
    public static  UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
