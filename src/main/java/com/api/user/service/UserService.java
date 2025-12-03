package com.api.user.service;

import com.api.user.dto.LoginRequestDTO;
import com.api.user.dto.LoginResponseDTO;
import com.api.user.dto.UserRequestDTO;
import com.api.user.dto.UserResponseDTO;
import com.api.user.entity.Role;
import com.api.user.entity.User;
import com.api.user.exception.DuplicationUserException;
import com.api.user.exception.LoginValidationException;
import com.api.user.exception.UserNotFoundException;
import com.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.api.user.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResponseDTO login(LoginRequestDTO loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(()-> new LoginValidationException("Invalid username or password"));

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());

        if (!passwordMatches){
            throw new LoginValidationException("Invalid username or password");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

        UserResponseDTO userResponse = mapToUserResponse(user);
        return new LoginResponseDTO(true, "login successful", userResponse, token);
    }

    public UserResponseDTO registerNewUser(UserRequestDTO userRequestDTO){
        if (userRepository.existsByUsername(userRequestDTO.getUsername())){
            throw new DuplicationUserException("User already exists with username: "+userRequestDTO.getUsername());
        }

        if (userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new DuplicationUserException("Email already exists: "+userRequestDTO.getEmail());
        }

        if (userRepository.existsByPhoneNumber(userRequestDTO.getPhoneNumber())){
            throw new DuplicationUserException("Phone number already exists: "+userRequestDTO.getPhoneNumber());
        }


        User user = mapToEntity(userRequestDTO);

        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public UserResponseDTO getUserById(Long id){
        User users = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id "+ id +" not found"));


        return mapToUserResponse(users);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserByEmail(String email){
        return userRepository.findByEmail(email).map(this::mapToUserResponse);
    }

    public Optional<UserResponseDTO> getUserByUsername(String username){
        return userRepository.findByUsername(username).map(this::mapToUserResponse);
    }

    public List<UserResponseDTO> getAllUserWithSameAddress(String address){
        return userRepository.findByAddress(address).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getUserByAge(Integer age){
        return userRepository.findByAge(age).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber).map(this::mapToUserResponse);
    }

    // here is to update all entire fields at once
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

        if (!existUser.getPhoneNumber().equals(updateUser.getPhoneNumber())){
            if (userRepository.existsByPhoneNumber(updateUser.getPhoneNumber())){
                throw new RuntimeException("phone number already exist: "+updateUser.getPhoneNumber());
            }
        }

        existUser.setUsername(updateUser.getUsername());
        existUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
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
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdateAt()
        );


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
                        if (userRepository.existsByPhoneNumber(newPhoneNumber)){
                            throw new DuplicationUserException("phone number already exist: "+newPhoneNumber);
                        }
                        userExist.setPhoneNumber(newPhoneNumber);
                    }
                    break;

                case "email":
                    String newEmail = String.valueOf(value);
                    if (!userExist.getEmail().equals(newEmail)){
                        if (userRepository.existsByEmail(newEmail)){
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



}
