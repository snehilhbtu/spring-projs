package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //           generic  param
    //             <----><--->
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String email,String username);

    Optional<User> findByUsername(String username);

    Boolean exitsByUsername(String username);

    Boolean existsByEmail(String email);

}
