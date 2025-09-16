package com.tesis.proyect.app.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IARequest {
    private String model;
    private List<IAMessage> messages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IAMessage {
        private String role;
        private String content;
    }
}
