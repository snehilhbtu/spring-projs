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
                        //authorise only get requests
                        authorize
                                .requestMatchers(HttpMethod.GET,"/api/posts/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                //.httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticatcationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }

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
==============================
🌐 1. USER LOGIN REQUEST
==============================
Client sends credentials to:
POST /api/auth/login
Request Body: { username, password }

      |
      v

==============================
🧠 2. AuthController.logIn()
==============================
Delegates to AuthServiceImpl.login()
-> Calls AuthenticationManager.authenticate()

      |
      v

==============================
🔍 3. AuthenticationManager
==============================
-> Internally uses:
   CustomUserDetailsService.loadUserByUsername()
-> Fetches user from DB
-> Returns UserDetails (email, password, roles)

      |
      v

==============================
🔑 4. JwtTokenProvider.generateToken()
==============================
-> Creates JWT using:
   - Subject (username)
   - Issue time
   - Expiry time
   - Secret key
-> Signs token and returns to controller

      |
      v

==============================
✅ 5. Controller Response
==============================
Returns:
{
  accessToken: "<JWT-TOKEN>",
  tokenType: "Bearer"
}

Client stores this token locally.

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

