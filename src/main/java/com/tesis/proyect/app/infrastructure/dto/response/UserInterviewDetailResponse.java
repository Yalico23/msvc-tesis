package com.tesis.proyect.app.infrastructure.dto.response;

import com.tesis.proyect.app.domain.models.UserInterview;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInterviewDetailResponse {
    private String id;
    private Integer score;
    private String state;
    private LocalDate date;
    private String userId;
    private String userName;
    private String interviewId;
    private String interviewTitle;
    private String s3KeyPath;
    private Integer duration;
    private List<Answer> answers;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer{
        private String questionText;
        private String responseText;
        private Integer points;
        private String description;
    }
}
