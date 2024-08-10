package com.springteam.backend.controller;

import com.springteam.backend.dto.AuthResponse;
import com.springteam.backend.dto.LoginDto;
import com.springteam.backend.dto.RegisterDto;
import com.springteam.backend.entity.Role;
import com.springteam.backend.entity.UserEntity;
import com.springteam.backend.exception.InvalidAuthInfoException;
import com.springteam.backend.exception.UserExistingException;
import com.springteam.backend.security.JwtGenerator;
import com.springteam.backend.service.IRoleService;
import com.springteam.backend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private IUserService userService;
    private IRoleService roleService;
    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder
            , IUserService userService, IRoleService roleService, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Validated @RequestBody RegisterDto registerDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidAuthInfoException("vui lòng nhập đầy đủ và chính xác thông tin");


        if (userService.isUserNameExisting(registerDto.getUsername()))
            throw new UserExistingException("username is taken");
        if (userService.isEmailExisting(registerDto.getEmail()))
            throw new UserExistingException("email is taken");

        Set<Role> role = new HashSet<>();
        role.add(roleService.getRoleByName("USER"));

        UserEntity userEntity = UserEntity.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(role)
                .build();
        userService.addNewUser(userEntity);
        return new ResponseEntity<>("register successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidAuthInfoException("vui lòng nhập đầy đủ và chính xác thông tin");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(jwtGenerator.generateToken(authentication))
                .role(authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet()))
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
