package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPasswordServicePort;
import com.lc.dentalcore.domain.api.ITokenServicePort;
import com.lc.dentalcore.domain.api.IUserServicePort;
import com.lc.dentalcore.domain.exception.EmailAlreadyExistsException;
import com.lc.dentalcore.domain.exception.InvalidCredentialsException;
import com.lc.dentalcore.domain.model.User;
import com.lc.dentalcore.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordServicePort passwordServicePort;
    private final ITokenServicePort tokenServicePort;

    @Override
    public void createUser(User user) {
        user.validate();
        userPersistencePort.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException();
        });
        user.encodePassword(passwordServicePort);
        userPersistencePort.saveUser(user);
    }

    @Override
    public String login(User user) {
        user.validate();
        User existingUser = userPersistencePort.findByEmail(user.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordServicePort.matches(user.getPassword(), existingUser.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return tokenServicePort.generateToken(existingUser);
    }
}
