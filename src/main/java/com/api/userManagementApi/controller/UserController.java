package com.api.userManagementApi.controller;

import com.api.userManagementApi.dto.LoginRequestDTO;
import com.api.userManagementApi.dto.LoginResponseDTO;
import com.api.userManagementApi.dto.UserRequestDTO;
import com.api.userManagementApi.dto.UserResponseDTO;
import com.api.userManagementApi.entity.User;
import com.api.userManagementApi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
// here is to get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> getAllUsers = userService.getAllUsers();
        return ResponseEntity.ok(getAllUsers);
    }
// here is to get user by using id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById (@PathVariable Long id){
         UserResponseDTO user = userService.getUserById(id);

         return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest){
        LoginResponseDTO login = userService.login(loginRequest);
        return ResponseEntity.ok(login);
    }

// here is to register new users
    @PostMapping("/registerUsers")
    public ResponseEntity<UserResponseDTO> registerUser (@Valid @RequestBody UserRequestDTO user){
        UserResponseDTO registerUser = userService.registerNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }
// here is get all detail of user using email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email){
        return userService.getUserByEmail(email).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
// here is get all detail of user using username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
// here is get all detail of user using phone number
    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<User> getUserBYPhoneNumber(@PathVariable String phoneNumber){
        return userService.getUserByPhoneNumber(phoneNumber).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
// here is to all with the same address
    @GetMapping("/address/{address}")
    public ResponseEntity<List<User>> findByAddress (@PathVariable String address){
        List<User> findByAddress = userService.getAllUserWithSameAddress(address);
        return ResponseEntity.ok(findByAddress);
    }
// here is to retrieve all with the same age
    @GetMapping("/age/{age}")
    public ResponseEntity<List<User>> findByAge(@PathVariable Integer age){
        List<User> findByAge = userService.getUserByAge(age);
        return ResponseEntity.ok(findByAge);
    }
// this is the end point for update entire system
    @PutMapping("/updateUsers/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user){
        User update = userService.updateUser(id,user);
        return ResponseEntity.ok(update);
    }
// this is the end point for the patch update of the system
    @PatchMapping("/patchUpdates/{id}")
    public ResponseEntity<UserResponseDTO> patchUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        UserResponseDTO patchUserUpdate = userService.patchUpdate(id, updates);
        return ResponseEntity.ok(patchUserUpdate);
    }
// this is the end point for deleting the system
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id){
        userService.delete(id);

        Map<String, Object> response = new HashMap<>();
        response.put("successful", true);
        response.put("message", "user delete successful ");
        response.put("id",id);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);

    }
// to is the final product release and push them all to the git i'm so tired enough see you tomorrow
    // my lovely java and intellij ide.....
}
