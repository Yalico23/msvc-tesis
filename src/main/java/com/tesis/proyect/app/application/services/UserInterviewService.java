package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.ListUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.input.userinterview.SaveUserInterviewUseCase;
import com.tesis.proyect.app.infrastructure.dto.response.UserInterviewDetailResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserInterviewService implements SaveUserInterviewUseCase, ListUserInterviewUseCase {

    private final SaveUserInterviewUseCase saveUserInterviewUseCase;
    private final ListUserInterviewUseCase listUserInterviewUseCase;

    public UserInterviewService(SaveUserInterviewUseCase saveUserInterviewUseCase, ListUserInterviewUseCase listUserInterviewUseCase) {
        this.saveUserInterviewUseCase = saveUserInterviewUseCase;
        this.listUserInterviewUseCase = listUserInterviewUseCase;
    }

    @Override
    public Mono<UserInterview> saveUserInterview(String userId, String userInterview) {
        return saveUserInterviewUseCase.saveUserInterview(userId, userInterview);
    }

    @Transactional
    @Override
    public Mono<UserInterview> finishUserInterview
            (Flux<FilePart> audios, FilePart fullVideo, String userId, String userInterview, String durationMinutes) {
        return saveUserInterviewUseCase.finishUserInterview
                (audios,fullVideo,userId,userInterview,durationMinutes);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<UserInterviewDetailResponse> findAll() {
        return listUserInterviewUseCase.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<UserInterviewDetailResponse> findByUserId(String userId) {
        return listUserInterviewUseCase.findByUserId(userId);
    }
}
