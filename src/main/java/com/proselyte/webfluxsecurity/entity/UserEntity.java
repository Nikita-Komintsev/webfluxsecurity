package com.proselyte.webfluxsecurity.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private String first_name;
    private String last_name;
    private boolean enabled;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }
}
