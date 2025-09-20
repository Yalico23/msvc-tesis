package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.InterviewService;
import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.infrastructure.dto.request.CreateInterviewRequest;
import com.tesis.proyect.app.infrastructure.dto.request.UpdateInterviewRequest;
import com.tesis.proyect.app.infrastructure.mappers.InterviewMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;
    private final InterviewMapper interviewMapper;

    @PreAuthorize("hasRole('RECLUTADOR')")
    @PostMapping("/create")
    public Mono<ResponseEntity<Interview>> createInterview
            (@Valid @RequestBody CreateInterviewRequest createInterviewRequest) {
        return interviewService.saveInterview(interviewMapper.toModel(createInterviewRequest))
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('RECLUTADOR')")
    @PutMapping("/update")
    public Mono<ResponseEntity<Interview>> updateInterview
            (@Valid @RequestBody UpdateInterviewRequest updateInterviewRequest) {
        return interviewService.updateInterview(interviewMapper.toModel(updateInterviewRequest))
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('RECLUTADOR')")
    @GetMapping("/listInterviews")
    public Mono<ResponseEntity<Flux<Interview>>> findAllInterviews() {
        Flux<Interview> interviews = interviewService.listInterviews();
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(interviews)
        );
    }

    @PreAuthorize("hasRole('PRACTICANTE')")
    @GetMapping("/listInterviewPracticante")
    public Mono<ResponseEntity<Interview>> findByUserId
            (@RequestParam("practicanteId") String practicanteId) {
        return interviewService.findByUserIdAssigned(practicanteId)
                .map(interview -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(interview));
    }
}
