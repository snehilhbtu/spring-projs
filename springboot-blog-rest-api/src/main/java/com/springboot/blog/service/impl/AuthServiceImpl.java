package com.springboot.blog.service.impl;

import com.springboot.blog.dto.LoginDto;
import com.springboot.blog.dto.RegisterDto;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public String login(LoginDto login) {

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsernameOrEmail(),
                login.getPassword()));

        /*
        this above function calls
        authManager --> DaoAuthProvider -->uses customUser class --> fetches user
        --> provider uses Bcrypt pass inside security to validate password
        */

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        //check if username/email exists
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Username already Exists");
        }

        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Email already Exists");
        }

        Set<Role> roles=new HashSet<>();
        Role role=roleRepository.findByName("ROLE_USER").get();
        roles.add(role);

        User user=new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully";
    }

}

/*
=======================================================================================
        🔐 JWT Authentication Flow in Spring Security - With Password Checking
=======================================================================================

        🔁 STEP 1: CLIENT SENDS LOGIN REQUEST
-------------------------------------
POST /api/auth/login
Body:
        {
        "usernameOrEmail": "john@example.com",
        "password": "123456"
        }

        ⬇️

STEP 2: AuthController receives request and calls authService.login()
----------------------------------------------------------------------

AuthController.java
--------------------
@PostMapping("/login")
public ResponseEntity<JWTAuthResponse> logIn(@RequestBody LoginDto loginDto) {
    String token = authService.login(loginDto); // 🔥 triggers login flow
    return ResponseEntity.ok(new JWTAuthResponse(token));
}

⬇️

STEP 3: AuthServiceImpl.login() creates Authentication Token
------------------------------------------------------------

Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        )
);

⬇️

STEP 4: AuthenticationManager triggers DaoAuthenticationProvider
-----------------------------------------------------------------
This is auto-wired behind the scenes. It uses:

        ➡️ CustomUserDetailsService (your class)
➡️ BCryptPasswordEncoder (your configured bean)

⬇️

STEP 5: DaoAuthenticationProvider does the heavy lifting:
        ----------------------------------------------------------

        ✔️ Calls your loadUserByUsername():
        ------------------------------------
CustomUserDetailsService.java
-----------------------------
UserDetails user = userRepository.findByUsernameOrEmail(...);

        ✔️ Then checks the password:
        -----------------------------
        passwordEncoder.matches(rawPassword, encodedPasswordFromDB)

Example:
        BCryptPasswordEncoder.matches("123456", "$2a$10$randomEncodedPassword")

✅ If password matches → return Authenticated Authentication object
❌ If not → throws BadCredentialsException

⬇️

STEP 6: AuthServiceImpl receives authenticated object and generates token
--------------------------------------------------------------------------

String token = jwtTokenProvider.generateToken(authentication);

⬇️

STEP 7: Controller returns JWT to client
----------------------------------------

        {
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR...",
        "tokenType": "Bearer"
        }

        =======================================================================================
        🔁 Summary:
        =======================================================================================

        1️⃣ UsernamePasswordAuthenticationToken → wraps raw login credentials
2️⃣ authenticationManager → sends it to DaoAuthenticationProvider
3️⃣ DaoAuthenticationProvider:
        - Loads user from DB using CustomUserDetailsService
    - Compares passwords using BCryptPasswordEncoder
4️⃣ On success → returns authenticated Authentication object
5️⃣ JwtTokenProvider → generates signed JWT with username
6️⃣ Controller returns token to client

=======================================================================================
        🔥 Bonus:
        - You configure the encoder in SecurityConfig:
@Bean public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

*/

/*
==============================
🔁 6. Protected Request
==============================
Client → GET /api/posts
Headers:
Authorization: Bearer <JWT-TOKEN>

      |
      v

==============================
🧱 7. JwtAuthenticationFilter
==============================
Intercepts every request
-> Extracts token from "Authorization" header

      |
      v

==============================
🔎 8. JwtTokenProvider.validateToken()
==============================
-> Validates structure, signature, expiry
-> If valid, extracts username

      |
      v

==============================
👤 9. Load User Again
==============================
-> CustomUserDetailsService.loadUserByUsername()
-> Get UserDetails + roles

      |
      v

==============================
🔐 10. Set Authentication
==============================
Creates:
  UsernamePasswordAuthenticationToken
Sets in:
  SecurityContextHolder

NOW the request is authenticated! ✅
*/

