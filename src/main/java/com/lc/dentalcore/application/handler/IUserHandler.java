package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.TokenResponseDTO;
import com.lc.dentalcore.application.dto.UserDTO;

public interface IUserHandler {
    void createUser(UserDTO userDTO);
    TokenResponseDTO login(UserDTO userDTO);
}
