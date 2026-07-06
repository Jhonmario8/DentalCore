package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.User;

public interface IUserPersistencePort {
    void saveUser(User user);
}
