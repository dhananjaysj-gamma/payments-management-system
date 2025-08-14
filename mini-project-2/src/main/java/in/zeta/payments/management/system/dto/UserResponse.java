package in.zeta.payments.management.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import in.zeta.payments.management.system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long userID;
    private String username;
    private String email;
    private Role role;
}
