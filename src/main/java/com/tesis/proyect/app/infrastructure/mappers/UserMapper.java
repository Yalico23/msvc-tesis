package com.tesis.proyect.app.infrastructure.mappers;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.infrastructure.documents.UserEntity;
import com.tesis.proyect.app.infrastructure.dto.request.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    User toModel(UserEntity userEntity);
    User toModel(CreateUserRequest request);
}
