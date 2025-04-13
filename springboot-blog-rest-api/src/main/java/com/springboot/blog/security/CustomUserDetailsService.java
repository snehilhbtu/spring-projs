package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//s9n0qc010482384
//authenticationFilter --> creates authenticationObject --> authenticationManager
//-->authentication provider (3 types)(which has support & authenticated methods) --> gives to userDetailsService(having loadByUsername method)
//--> which fetches userDetails implements User
@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException{

        //if user exists
        User user=userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail).
                orElseThrow(()->new UsernameNotFoundException("Username Or Email not found "+usernameOrEmail));

        //set of granted auth for creating a user
        Set<GrantedAuthority> authorities= user.getRoles().stream().map(
                (role)-> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);

    }

}
