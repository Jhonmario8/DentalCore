package com.lc.dentalcore.application.mapper;

import com.lc.dentalcore.application.dto.UserDTO;
import com.lc.dentalcore.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {

    User toDomain(UserDTO userDTO);

    UserDTO toDTO(User user);

}
