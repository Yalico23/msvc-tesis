package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.SaveUserInterviewUseCase;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserInterviewService implements SaveUserInterviewUseCase {

    private final SaveUserInterviewUseCase saveUserInterviewUseCase;

    public UserInterviewService(SaveUserInterviewUseCase saveUserInterviewUseCase) {
        this.saveUserInterviewUseCase = saveUserInterviewUseCase;
    }

    @Transactional
    @Override
    public Mono<UserInterview> saveUserInterview(Flux<FilePart> audios, FilePart fullVideo, String userId, String userInterview) {
        return saveUserInterviewUseCase.saveUserInterview(audios,fullVideo,userId,userInterview);
    }

    @Override
    public Mono<String> devolverPropmt(String prompt) {
        return saveUserInterviewUseCase.devolverPropmt(prompt);
    }
}
