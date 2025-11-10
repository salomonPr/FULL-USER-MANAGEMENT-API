package com.api.user;

import com.api.user.entity.User;
import com.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagenentApiApplication  {

	public static void main(String[] args) {
		SpringApplication.run(UserManagenentApiApplication.class, args);
	}



}
