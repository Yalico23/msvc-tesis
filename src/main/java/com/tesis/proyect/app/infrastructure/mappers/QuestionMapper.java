package com.tesis.proyect.app.infrastructure.mappers;

import com.tesis.proyect.app.domain.models.Question;
import com.tesis.proyect.app.infrastructure.dto.request.CreateInterviewRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question toModel(CreateInterviewRequest.QuestionRequest request);
}
