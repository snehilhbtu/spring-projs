package com.springboot.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity //<- method lvl security
//^^^it internally has pre,postauth tags etc
public class SecurityConfig {

    /*
    basic auth filter will create a instance of username password and will pass it to authentication mananger
    it will return an auth object which will eventually be saved in security context holder
     */

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        //authorise only get requests
                        authorize
                                .requestMatchers(HttpMethod.GET,"/api/posts/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return  http.build();
    }

    //  Define an in-memory user
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails snehil=User.builder().
                username("snehil")
                        .password(passwordEncoder().encode("snehil"))
                        .roles("USER")
                        .build();

        UserDetails admin=User.builder()
                .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build();
        //we can create a single user inside this InMemory also using User.withUsername etc methods
        return new InMemoryUserDetailsManager(snehil,admin);

    }

    // Define password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


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
