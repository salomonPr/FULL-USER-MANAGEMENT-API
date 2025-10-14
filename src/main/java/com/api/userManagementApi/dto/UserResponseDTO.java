package com.api.userManagementApi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDTO {


        private Long id;
        private String username;
        private String fullName;
        private String phoneNumber;
        private String email;
        private String address;
        private Integer age;
        private LocalDateTime createdAt;
        private LocalDateTime updateAt;

}
