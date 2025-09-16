package com.tesis.proyect.app.domain.ports.input.userinterview;

import com.tesis.proyect.app.domain.models.UserInterview;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaveUserInterviewUseCase {
    Mono<UserInterview> saveUserInterview (Flux<FilePart> audios, FilePart fullVideo, String userId, String userInterview);
    Mono<String> devolverPropmt(String prompt);
}
