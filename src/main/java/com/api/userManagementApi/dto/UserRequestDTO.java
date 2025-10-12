package com.api.userManagementApi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRequestDTO {

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        private String username;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String password;

        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
        private String phoneNumber;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @NotBlank(message = "Address is required")
        @Size(max = 200, message = "Address cannot exceed 200 characters")
        private String address;

        @NotNull(message = "Age is required")
        @Min(value = 13, message = "Age must be at least 13")
        @Max(value = 120, message = "Age must be realistic")
        private Integer age;

}
