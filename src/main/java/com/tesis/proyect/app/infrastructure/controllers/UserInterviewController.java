package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserInterviewService;
import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.infrastructure.dto.response.ListUserInterviewResponse;
import com.tesis.proyect.app.infrastructure.mappers.UserInterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final UserInterviewMapper mapper;

    //@PreAuthorize("hasRole('RECLUTADOR')")
    @PostMapping("/assignInterviewToPracticante")
    public Mono<ResponseEntity<UserInterview>> assignInterviewToPracticante
    (@RequestParam("userId") String userId,
     @RequestParam("interviewId") String interviewId) {
        return userInterviewService.saveUserInterview(userId, interviewId)
                .map(updatedUser -> ResponseEntity.ok()
                        .body(updatedUser));
    }

    //@PreAuthorize("hasAnyRole('RECLUTADOR','PRACTICANTE')")
    @PostMapping(value = "/finishInterview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UserInterview> finishInterview
            (@RequestPart("audios") Flux<FilePart> audios,
             @RequestPart("video") FilePart video,
             @RequestPart("userId") String userId,
             @RequestPart("interviewId") String interviewId,
             @RequestPart("durationMinutes") String durationMinutes) {
        return userInterviewService.finishUserInterview(audios, video, userId, interviewId, durationMinutes);
    }

    //@PreAuthorize("hasRole('RECLUTADOR')")
    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<ListUserInterviewResponse>>> findAllByInterviewId() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userInterviewService.findAll()
                                .map(mapper::toListResponse))
        );
    }
}
