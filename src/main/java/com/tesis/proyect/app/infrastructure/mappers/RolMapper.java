package com.tesis.proyect.app.infrastructure.mappers;

import com.tesis.proyect.app.domain.models.Rol;
import com.tesis.proyect.app.infrastructure.documents.RolEntity;
import com.tesis.proyect.app.infrastructure.dto.request.CreateRolRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolEntity toEntity (Rol rol);
    Rol toModel (RolEntity rolEntity);
    Rol toModel (CreateRolRequest request);
}
