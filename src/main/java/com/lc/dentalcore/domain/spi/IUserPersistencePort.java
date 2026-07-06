package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {
    void saveUser(User user);
    Optional<User> findByEmail(String email);
}
