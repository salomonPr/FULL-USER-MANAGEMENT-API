package com.api.user.dto;

import com.api.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private Integer age;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;



}
