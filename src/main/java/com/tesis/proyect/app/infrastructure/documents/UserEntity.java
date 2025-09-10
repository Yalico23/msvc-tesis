package com.tesis.proyect.app.infrastructure.documents;

import com.tesis.proyect.app.domain.models.UserInterview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String name;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String token;
    private LocalDate creationDate;
    private Boolean active;
    private RolEntity role;
    private List<InterviewEntity> interviews;
    private List<UserInterview> userInterviews;
}
