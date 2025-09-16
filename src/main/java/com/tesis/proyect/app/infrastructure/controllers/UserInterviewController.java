package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserInterviewService;
import com.tesis.proyect.app.domain.models.UserInterview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userinterview")
public class UserInterviewController {

    private final UserInterviewService userInterviewService;

    @PreAuthorize("hasRole('RECLUTADOR')")
    @PostMapping(value = "/finishInterview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UserInterview> finishInterview
            (@RequestPart("audios") Flux<FilePart> audios,
             @RequestPart("video") FilePart video,
             @RequestPart("userId") String userId,
             @RequestPart("interviewId") String interviewId) {
        return userInterviewService.saveUserInterview(audios, video, userId, interviewId);
    }
}
