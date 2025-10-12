package com.api.userManagementApi;

import com.api.userManagementApi.entity.User;
import com.api.userManagementApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApiApplication
//        implements CommandLineRunner
                                          {

//    @Autowired
//    private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApiApplication.class, args);



	}

//    @Override
//    public void run(String... args) throws Exception {
//
//        User userTest = new User();
//        userTest.setUsername("salomon250");
//        userTest.setPassword("salomon250");
//        userTest.setFullName("Nkurunziza Salomon");
//        userTest.setPhoneNumber("0786261319");
//        userTest.setEmail("nkurunzizasalomon@gmail.com");
//        userTest.setAddress("kigali, kanomber");
//        userTest.setAge(25);
//        User saveUser = userRepository.save(userTest);
//        System.out.println("user saved successful: "+saveUser.getId());
//
//        User found = userRepository.findById(saveUser.getId()).orElse(null);
//        if (found != null) {
//            System.out.println("✅ User found: " + found.getUsername());
//        }
//    }
}
