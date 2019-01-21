package com.callicoder.controller;

import com.callicoder.config.jwt.JwtTokenProvider;
import com.callicoder.exception.AppException;
import com.callicoder.model.Role;
import com.callicoder.model.RoleName;
import com.callicoder.model.User;
import com.callicoder.payload.ApiResponse;
import com.callicoder.payload.JwtAuthenticationResponse;
import com.callicoder.payload.LoginRequest;
import com.callicoder.payload.SignUpRequest;
import com.callicoder.repository.RoleRepository;
import com.callicoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail() , loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity(new ApiResponse(false , "Username is already taken!") , HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false , "Email Address already in use!") , HttpStatus.BAD_REQUEST);
        }
        System.out.println("-------------------------> Create user");
        // createing user's account
        User user = new User(signUpRequest.getName() , signUpRequest.getUsername() , signUpRequest.getEmail() , signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(role));

        User result = userRepository.save(user);
        System.out.println("-------------------------> user created" + result);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
                                                   .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true , "User register successfully"));

    }
}
