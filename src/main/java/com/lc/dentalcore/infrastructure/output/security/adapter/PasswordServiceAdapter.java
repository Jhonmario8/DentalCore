package com.lc.dentalcore.infrastructure.output.security.adapter;

import com.lc.dentalcore.domain.api.IPasswordServicePort;
import com.lc.dentalcore.infrastructure.constants.InfrastructureConstants;
import com.lc.dentalcore.infrastructure.exception.InfrastructureException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordServiceAdapter implements IPasswordServicePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InfrastructureException(InfrastructureConstants.MSG_PASSWORD_CANNOT_BE_NULL_OR_EMPTY, InfrastructureConstants.BAD_REQUEST);
        }
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hashedPassword) {
        if (password == null || password.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            throw new InfrastructureException(InfrastructureConstants.MSG_PASSWORD_CANNOT_BE_NULL_OR_EMPTY, InfrastructureConstants.BAD_REQUEST);
        }
        return passwordEncoder.matches(password, hashedPassword);
    }
}
