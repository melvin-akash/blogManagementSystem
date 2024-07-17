package com.melvin.blogManagementSystem.controller;

import com.melvin.blogManagementSystem.dto.LoginRequest;
import com.melvin.blogManagementSystem.dto.LoginResponse;
import com.melvin.blogManagementSystem.dto.UserDto;
import com.melvin.blogManagementSystem.entity.User;
import com.melvin.blogManagementSystem.jwt.JwtUtils;
import com.melvin.blogManagementSystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.melvin.blogManagementSystem.entity.Role.USER;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/security")
public class DemoController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;

    //api working
    @GetMapping("/demo")
    public String demo(){
        return "hello from demo controller";
    }

    //api not working
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String user(){
        return "hello from user controller";
    }

    //api not working
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(){
        return "hello from admin controller";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setRole(USER);

        userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account registered");
        response.put("user", userDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register2")
    public ResponseEntity<?> registerUser2(@RequestBody UserDto userDto){

        var user = User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(USER)
                .build();

        userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account registered");
        response.put("user", userDto.getEmail());
        response.put("Role", USER);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.ok(response);
    }

}
