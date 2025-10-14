package com.api.userManagementApi.service;

import com.api.userManagementApi.dto.LoginRequestDTO;
import com.api.userManagementApi.dto.LoginResponseDTO;
import com.api.userManagementApi.dto.UserRequestDTO;
import com.api.userManagementApi.dto.UserResponseDTO;
import com.api.userManagementApi.entity.User;
import com.api.userManagementApi.exception.DuplicationUserException;
import com.api.userManagementApi.exception.LoginValidationException;
import com.api.userManagementApi.exception.UserNotFoundException;
import com.api.userManagementApi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registerNewUser(UserRequestDTO userRequestDTO){
        log.info("creating new user with username{} ",userRequestDTO.getUsername());
        if (userRepository.existsByUsername(userRequestDTO.getUsername())){
            throw new RuntimeException("user already exist with username: "+userRequestDTO.getUsername());
        }

        if (userRepository.existsByEmail(userRequestDTO.getEmail())){
            log.warn("Attempt to create user with already username exist{} ",userRequestDTO.getUsername());
            throw new RuntimeException("email already exist: "+userRequestDTO.getEmail());
        }

        User user = mapToEntity(userRequestDTO);

        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("user create successful with id{} ",savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    public UserResponseDTO getUserById(Long id){
        log.info("fetch user by user id {} ",id);
        User users = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("user with: "+id+" not found! "));
        

        return mapToUserResponse(users);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUserWithSameAddress(String address){
        return userRepository.findByAddress(address);
    }

    public List<User> getUserByAge(Integer age){
        return userRepository.findByAge(age);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    // here is to update entire all fields at once
    public User updateUser(Long id, User updateUser){
        User existUser = userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found: "+id));

       if (!existUser.getUsername().equals(updateUser.getUsername())){
           if (userRepository.existsByUsername(updateUser.getUsername())){
               throw new RuntimeException("user already exist: "+updateUser.getUsername());
           }
       }

       if (!existUser.getEmail().equals(updateUser.getEmail())){
           if (userRepository.existsByEmail(updateUser.getEmail())){
               throw new RuntimeException("email already exist "+updateUser.getEmail());
           }
       }

       if (!existUser.getPhoneNumber().equals(updateUser.getEmail())){
           if (userRepository.existsByPhoneNumber(updateUser.getPhoneNumber())){
               throw new RuntimeException("phone number already exist: "+updateUser.getPhoneNumber());
           }
       }

       existUser.setUsername(updateUser.getUsername());
       existUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
       existUser.setPassword(updateUser.getPassword());
       existUser.setFullName(updateUser.getFullName());
       existUser.setPhoneNumber(updateUser.getPhoneNumber());
       existUser.setEmail(updateUser.getEmail());
       existUser.setAddress(updateUser.getAddress());
       existUser.setAge(updateUser.getAge());

       return userRepository.save(existUser);
    }

    private User mapToEntity(UserRequestDTO userRequestDTO){

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setFullName(userRequestDTO.getFullName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setEmail(userRequestDTO.getEmail());
        user.setAddress(userRequestDTO.getAddress());
        user.setAge(userRequestDTO.getAge());

        return user;

    }

    private UserResponseDTO mapToUserResponse(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getAddress(),
                user.getAge(),
                user.getCreatedAt(),
                user.getUpdateAt());


    }

    @Transactional
    public UserResponseDTO patchUpdate(Long id, Map<String, Object> update){
        User userExist = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found with id: "+id));
        update.forEach((key,value)->
        {
            if (value == null) {
                return;
            }
            switch (key){
                case "username":
                    String newUser = String.valueOf(value);
                    if (!userExist.getUsername().equals(newUser)){
                        if (userRepository.existsByUsername(newUser)){
                            throw new DuplicationUserException("user already exist with username: "+newUser);
                        }
                        userExist.setUsername(newUser);
                    }
                    break;

                case "password":
                    String newPassword = String.valueOf(value);
                    userExist.setPassword(passwordEncoder.encode(newPassword));
                    break;

                case "fullName":
                    String newFullName = String.valueOf(value);
                    userExist.setFullName(newFullName);
                    break;

                case "phoneNumber":
                    String newPhoneNumber = String.valueOf(value);
                    if (!userExist.getPhoneNumber().equals(newPhoneNumber)){
                        if (userRepository.existsByUsername(newPhoneNumber)){
                            throw new DuplicationUserException("phone number already exist: "+newPhoneNumber);
                        }
                        userExist.setPhoneNumber(newPhoneNumber);
                    }
                    break;

                case "email":
                    String newEmail = String.valueOf(value);
                    if (!userExist.getPhoneNumber().equals(newEmail)){
                        if (userRepository.existsByUsername(newEmail)){
                            throw new DuplicationUserException("this email already: "+newEmail);
                        }
                        userExist.setEmail(newEmail);
                    }
                    break;

                case "address":
                    String newAddress = String.valueOf(value);
                    userExist.setAddress(newAddress);
                    break;

                case "age":
                    Integer newAge;
                    if (value instanceof Integer){
                        newAge = (Integer) value;
                    }else if (value instanceof String){
                        try {
                            newAge = Integer.parseInt((String) value);
                        }catch (NumberFormatException e){
                           throw  new RuntimeException("invalid age type: "+value.getClass().getName());
                        }

                    } else if (value instanceof Number) {
                        newAge = ((Number) value).intValue();
                        
                    }else {
                        throw new RuntimeException("invalid age type: "+value.getClass().getName());
                    }

                    if (newAge<13 || newAge>120){
                        throw new RuntimeException("age must be between 13 and 120");
                    }

                    userExist.setAge(newAge);
                    break;

                default:
                    throw new RuntimeException("unknown  field "+key);
            }
        });

        User updated = userRepository.save(userExist);
        return mapToUserResponse(updated);
    }

    public void delete(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("user id not found: "+id));

        userRepository.delete(user);

        if (userRepository.existsById(id)){
            throw new RuntimeException("fail to delete ");
        }
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest){

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(()-> new LoginValidationException("Invalid username or password"));

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());
        if (!passwordMatches){
            throw new LoginValidationException("Invalid username or password ");
        }

        UserResponseDTO userResponseDTO = mapToUserResponse(user);
        return new LoginResponseDTO(true,"Login successful", userResponseDTO);

    }

}
