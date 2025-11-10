package com.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponseDTO {



        private boolean success;
//        private String token;
        private UserResponseDTO user;
        private String message;
        private LocalDateTime time;

        // constructor for successful login
    public LoginResponseDTO(boolean success, String message, UserResponseDTO user){
        this.success = success;
        this.message = message;
        this.user = user;
        this.time = LocalDateTime.now();
    }

        // constructor for login fail
    public LoginResponseDTO(boolean success, String message){
        this.success = success;
        this.message = message;
        this.user = null;
        this.time = LocalDateTime.now();

    }


}
