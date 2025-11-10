package com.api.user.repository;

import com.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {



        Optional<User> findByEmail(String email);
        boolean existsByEmail(String email);

        Optional<User> findByUsername(String username);
        boolean existsByUsername(String username);

        Optional<User> findByPhoneNumber(String phoneNumber);
        boolean existsByPhoneNumber(String phoneNumber);

        List<User> findByAddress(String address);
        List<User> findByAge (Integer age);


}
