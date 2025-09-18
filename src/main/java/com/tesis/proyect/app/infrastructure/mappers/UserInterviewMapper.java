package com.tesis.proyect.app.infrastructure.mappers;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.infrastructure.documents.UserInterviewEntity;
import com.tesis.proyect.app.infrastructure.dto.response.ListUserInterviewResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInterviewMapper {

    UserInterviewEntity toEntity (UserInterview userInterview);
    UserInterview toDomain (UserInterviewEntity userInterview);
    ListUserInterviewResponse toListResponse (UserInterview userInterview);
}
