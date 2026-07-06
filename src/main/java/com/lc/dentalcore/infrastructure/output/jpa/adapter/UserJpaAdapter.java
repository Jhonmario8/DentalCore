package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.User;
import com.lc.dentalcore.domain.spi.IUserPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository repository;
    private final IUserEntityMapper mapper;

    @Override
    public void saveUser(User user) {
        repository.save(mapper.toEntity(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }
}
