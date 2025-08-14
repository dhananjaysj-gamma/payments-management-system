package in.zeta.payments.management.system.entity;

import in.zeta.payments.management.system.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank(message = "Email cannot be null")
    @Column(name = "email", unique = true)
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;

    @NotNull(message = "Role must be provided")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
