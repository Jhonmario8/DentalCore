package com.lc.dentalcore.infrastructure.output.jpa.mapper;

import com.lc.dentalcore.domain.model.User;
import com.lc.dentalcore.infrastructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserEntityMapper {

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);

}
