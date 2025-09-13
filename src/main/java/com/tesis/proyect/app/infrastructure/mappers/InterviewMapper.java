package com.tesis.proyect.app.infrastructure.mappers;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.infrastructure.documents.InterviewEntity;
import com.tesis.proyect.app.infrastructure.dto.request.CreateInterviewRequest;
import com.tesis.proyect.app.infrastructure.dto.request.UpdateInterviewRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface InterviewMapper {
    InterviewEntity toEntity (Interview interview);
    Interview toModel (InterviewEntity interviewEntity);
    Interview toModel (CreateInterviewRequest createInterviewRequest);
    Interview toModel (UpdateInterviewRequest updateInterviewRequest);
}
