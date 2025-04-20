package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticatcationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;

@Configuration
@EnableMethodSecurity //<- method lvl security
//^^^it internally has pre,post auth tags etc
public class SecurityConfig {

    /*
    basic auth filter will create a instance of username password and will pass it to authentication manager
    it will return an auth object which will eventually be saved in security context holder
     */

    private UserDetailsService userDetailsService;

    private JwtAuthenticatcationEntryPoint authenticatcationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticatcationEntryPoint authenticatcationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticatcationEntryPoint=authenticatcationEntryPoint;
        this.authenticationFilter=authenticationFilter;
    }

    // Define password encoder
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //getting authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        //authorise only get requests, post for login/register
                        authorize
                                .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                //.httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticatcationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        //this tells to add filter before
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }
/*
         +--------------------+
         |  Incoming Request  |
         +--------------------+
                   |
                   v
     +-------------------------------+
     | SecurityFilterChain from Config|
     +-------------------------------+
                   |
       ┌──────────────┬────────────────┐
       ↓              ↓                ↓
  Login Route     Public Route     Protected Route
 (/api/auth/**)   (e.g. GET /posts)  (e.g. /api/data)
       ↓              ↓                ↓
Bypass JWT filter Permit All     → JwtAuthenticationFilter
       ↓                              ↓
AuthManager.authenticate()       Valid Token? → Yes → Set SecurityContext
       ↓                                        ↓
DaoAuthProvider                             → Controller
       ↓
LoadUserByUsername()
       ↓
PasswordEncoder.matches()
       ↓
Return JWT token (JWTAuthResponse)

 */


/*
===========================================================================================
🔐 SecurityConfig Class - Central Config for Spring Security (JWT Based)
===========================================================================================

🧠 WHAT IS THIS CLASS FOR?
---------------------------
This configures:
✔️ How Spring Security handles requests
✔️ Which routes are public vs protected
✔️ How passwords are encoded
✔️ How Spring handles unauthorized access
✔️ Attaches custom JWT filter before Spring’s default filters

===========================================================================================
🔩 CLASS-LEVEL ANNOTATIONS:
===========================================================================================

@Configuration
    → Tells Spring this is a configuration class.

@EnableMethodSecurity
    → Enables method-level security (@PreAuthorize, @PostAuthorize, etc.)

===========================================================================================
🧱 CONSTRUCTOR INJECTION:
===========================================================================================

This brings in:
🔹 userDetailsService            → used by DaoAuthenticationProvider to load user
🔹 authenticatcationEntryPoint  → sends 401 if unauthorized
🔹 authenticationFilter         → custom JWT filter that checks for valid token

===========================================================================================
🔑 PASSWORD ENCODER BEAN:
===========================================================================================

@Bean
public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

Why?
🔐 It hashes passwords when saving a user
🔁 It compares password hashes during login using:
   ➤ passwordEncoder.matches(rawPassword, hashedPassword)

===========================================================================================
⚙️ AUTHENTICATION MANAGER:
===========================================================================================

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config)

Why?
🧠 Needed to manually call authenticationManager.authenticate()
    → You do this in AuthServiceImpl during login

===========================================================================================
🔐 SECURITY FILTER CHAIN CONFIGURATION:
===========================================================================================

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http)

➡️ This is the MAIN method configuring Spring Security.
Let’s break down what's happening 👇

-----------------------------------------
1. CSRF Disabled (for stateless REST API)
-----------------------------------------
http.csrf(csrf -> csrf.disable());

-----------------------------------------
2. Public Routes
-----------------------------------------
http.authorizeHttpRequests(authorize ->
    authorize
        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
        .requestMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated()
);

🧠 Meaning:
- GET /api/posts/** → Public (no auth required)
- /api/auth/** (login, signup) → Public
- Everything else → Needs JWT token

-----------------------------------------
3. Custom Exception Handler
-----------------------------------------
http.exceptionHandling(exception ->
    exception.authenticationEntryPoint(authenticatcationEntryPoint)
);

🛑 If user hits a protected route without JWT → 401 sent by `JwtAuthenticatcationEntryPoint`

-----------------------------------------
4. Session Management
-----------------------------------------
http.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
);

💡 Why?
➡️ Because we use **JWT**, not server sessions

-----------------------------------------
5. Add JWT Filter Before Spring's Default
-----------------------------------------
http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

🔥 This is CRITICAL:
- `JwtAuthenticationFilter` checks Authorization header
- Validates token, sets security context (like login)
- Placed BEFORE Spring's UsernamePasswordAuthenticationFilter (which is for form-based login)

-----------------------------------------
6. Build and Return Security Chain
-----------------------------------------
return http.build();

===========================================================================================
📍 When and Where to Use this Class?
===========================================================================================

✅ ALWAYS needed in Spring Boot apps using:
- JWT Token Authentication
- Custom filters
- Stateless APIs
- Role-based or method-level auth (@PreAuthorize etc.)

📦 Automatically loaded by Spring Boot as part of Component Scan

===========================================================================================
*/


    //  Define an in-memory user, not usable since using db auth
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails snehil=User.builder().
//                username("snehil")
//                        .password(passwordEncoder().encode("snehil"))
//                        .roles("USER")
//                        .build();
//
//        UserDetails admin=User.builder()
//                .username("admin")
//                        .password(passwordEncoder().encode("admin"))
//                        .roles("ADMIN")
//                        .build();
//        //we can create a single user inside this InMemory also using User.withUsername etc methods
//        return new InMemoryUserDetailsManager(snehil,admin);
//
//    }


}
/*
 * This security configuration sets up role-based access control using Spring Security.
 *
 * - Disables CSRF protection, which is suitable for stateless REST APIs (especially when not using browser-based forms).
 * - Permits all users (authenticated or not) to perform GET requests to "/api/posts/**".
 * - Requires authentication for any other type of request to any endpoint.
 * - Uses HTTP Basic authentication to verify users via credentials in the request headers.
 *
 * Example configuration:
 * - Restrict POST requests to "/api/posts/**" to users with the ADMIN role only.
 * http
 *     .csrf(csrf -> csrf.disable())
 *     .authorizeHttpRequests(authorize -> authorize
 *         .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
 *         .requestMatchers(HttpMethod.POST, "/api/posts/**").hasRole("ADMIN")
 *         .anyRequest().authenticated()
 *     )
 *     .httpBasic(Customizer.withDefaults());
 *
 * This configuration uses Spring Security's lambda-based DSL and SecurityFilterChain bean
 * for clean, declarative access control in a modern Spring Boot application.
 */

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

