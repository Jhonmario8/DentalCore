package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPasswordServicePort;
import com.lc.dentalcore.domain.api.IUserServicePort;
import com.lc.dentalcore.domain.model.User;
import com.lc.dentalcore.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordServicePort passwordServicePort;

    @Override
    public void createUser(User user) {
        user.validate();
        user.encodePassword(passwordServicePort);
        userPersistencePort.saveUser(user);
    }
}
