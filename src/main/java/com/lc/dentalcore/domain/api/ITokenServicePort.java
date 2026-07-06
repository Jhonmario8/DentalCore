package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.User;

import java.util.Map;

public interface ITokenServicePort {

    String generateToken(User user);

    Map<String , Object> validateToken(String token);

}
