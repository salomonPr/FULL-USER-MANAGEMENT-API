package com.api.userManagementApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
