package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.User;

public interface IUserServicePort {
    void createUser(User user);
    String login(User user);
}
