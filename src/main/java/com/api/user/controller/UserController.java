package com.api.user.controller;

import com.api.user.dto.LoginRequestDTO;
import com.api.user.dto.LoginResponseDTO;
import com.api.user.dto.UserRequestDTO;
import com.api.user.dto.UserResponseDTO;
import com.api.user.entity.User;
import com.api.user.service.UserService;
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
        System.out.println(loginRequest.getUsername());
        LoginResponseDTO login = userService.login(loginRequest);
        return ResponseEntity.ok(login);
    }

    // here is to register new users
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser (@Valid @RequestBody UserRequestDTO user){

        System.out.println("=== REGISTER ENDPOINT CALLED ===");
        System.out.println("Username: " + user.getUsername());
        UserResponseDTO registerUser = userService.registerNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }
    // here is get all detail of user using email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getByEmail(@PathVariable String email){
        return userService.getUserByEmail(email).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
    // here is get all detail of user using username
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
    // here is get all detail of user using phone number
    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<UserResponseDTO> getUserBYPhoneNumber(@PathVariable String phoneNumber){
        return userService.getUserByPhoneNumber(phoneNumber).map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }
    // here is to all with the same address
    @GetMapping("/address/{address}")
    public ResponseEntity<List<UserResponseDTO>> findByAddress (@PathVariable String address){
        List<UserResponseDTO> findByAddress = userService.getAllUserWithSameAddress(address);
        return ResponseEntity.ok(findByAddress);
    }
    // here is to retrieve all with the same age
    @GetMapping("/age/{age}")
    public ResponseEntity<List<UserResponseDTO>> findByAge(@PathVariable Integer age){
        List<UserResponseDTO> findByAge = userService.getUserByAge(age);
        return ResponseEntity.ok(findByAge);
    }
    // this is the end point for an update entire system
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
// to is the final product release and push them all to the git I'm so tired enough to see you tomorrow
    // my lovely java and intellij ide.....
}
