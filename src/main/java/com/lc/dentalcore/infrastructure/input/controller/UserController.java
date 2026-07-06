package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.TokenResponseDTO;
import com.lc.dentalcore.application.dto.UserDTO;
import com.lc.dentalcore.application.handler.IUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserHandler userHandler;

    @PostMapping()
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        userHandler.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestBody UserDTO userDTO) {
        TokenResponseDTO token = userHandler.login(userDTO);
        return ResponseEntity.ok(token);
    }


}
