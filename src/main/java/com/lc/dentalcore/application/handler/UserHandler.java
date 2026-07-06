package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.TokenResponseDTO;
import com.lc.dentalcore.application.dto.UserDTO;
import com.lc.dentalcore.application.mapper.IUserMapper;
import com.lc.dentalcore.domain.api.IUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserMapper mapper;

    @Override
    public void createUser(UserDTO userDTO) {
        userServicePort.createUser(mapper.toDomain(userDTO));
    }

    @Override
    public TokenResponseDTO login(UserDTO userDTO) {
        String token = userServicePort.login(mapper.toDomain(userDTO));
        return new TokenResponseDTO(token);
    }
}
