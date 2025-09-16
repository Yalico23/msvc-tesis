package com.tesis.proyect.app.infrastructure.config;

import com.tesis.proyect.app.application.services.InterviewService;
import com.tesis.proyect.app.application.services.RolService;
import com.tesis.proyect.app.application.services.UserInterviewService;
import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.application.usecases.interview.ListInterviewUseCaseImpl;
import com.tesis.proyect.app.application.usecases.interview.SaveInterviewUseCaseImpl;
import com.tesis.proyect.app.application.usecases.rol.CreateRolUseCaseImpl;
import com.tesis.proyect.app.application.usecases.user.CreateUserUseCaseImpl;
import com.tesis.proyect.app.application.usecases.user.FindUserUSerCaseImpl;
import com.tesis.proyect.app.application.usecases.userinterview.SaveUserInterviewUseCaseImpl;
import com.tesis.proyect.app.domain.ports.output.*;
import com.tesis.proyect.app.infrastructure.adapters.AwsExternalServiceAdapter;
import com.tesis.proyect.app.infrastructure.adapters.IAExternalServiceAdapter;
import com.tesis.proyect.app.infrastructure.adapters.WhisperExternalServiceAdapter;
import com.tesis.proyect.app.infrastructure.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class ApplicationConfig {
    @Bean
    public UserRepositoryPort userRepositoryPort(UserEntityAdapter userEntityAdapter) {
        return userEntityAdapter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepositoryPort userRepositoryPort, RolRepositoryPort rolRepositoryPort, PasswordEncoder passwordEncoder) {
        return new UserService(
                new CreateUserUseCaseImpl(userRepositoryPort, rolRepositoryPort, passwordEncoder),
                new FindUserUSerCaseImpl(userRepositoryPort)
        );
    }

    @Bean
    public RolRepositoryPort rolRepositoryPort(RolEntityAdapter rolEntityAdapter) {
        return rolEntityAdapter;
    }

    @Bean
    public RolService rolService(RolRepositoryPort rolRepositoryPort) {
        return new RolService(
                new CreateRolUseCaseImpl(rolRepositoryPort)
        );
    }

    @Bean
    public InterviewRepositoryPort interviewRepositoryPort(InterviewEntityAdapter interviewEntityAdapter) {
        return interviewEntityAdapter;
    }

    @Bean
    public InterviewService interviewService(InterviewRepositoryPort interviewRepositoryPort) {
        return new InterviewService(
                new SaveInterviewUseCaseImpl(interviewRepositoryPort),
                new ListInterviewUseCaseImpl(interviewRepositoryPort)
        );
    }

    @Bean
    public UserInterviewRepositoryPort userInterviewRepositoryPort(UserInterviewEntityAdapter interviewEntityAdapter) {
        return interviewEntityAdapter;
    }

    @Bean
    public UserInterviewService userInterviewService
            (WhisperExternalServicePort whisperExternalServicePort,
             UserInterviewRepositoryPort userInterviewRepositoryPort,
             AwsExternalServicePort awsExternalServicePort,
             IAExternalServicePort iaExternalServicePort) {
        return new UserInterviewService(
                new SaveUserInterviewUseCaseImpl
                        (whisperExternalServicePort,
                                userInterviewRepositoryPort,
                                awsExternalServicePort,
                                iaExternalServicePort)
        );
    }

    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    public WhisperExternalServicePort whisperExternalServicePort(WebClient webClient) {
        return new WhisperExternalServiceAdapter(webClient);
    }

    @Value("${aws.access-key-id}")
    private String awsAccessKey;
    @Value("${aws.secret-access-key}")
    private String awsSecretKey;
    @Value("${aws.region}")
    private String region;

    @Bean
    public S3AsyncClient getS3AsyncClient() {
        AwsCredentials basicAwsCredential = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        return S3AsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create("https://s3.us-east-2.amazonaws.com"))
                .credentialsProvider(StaticCredentialsProvider.create(basicAwsCredential))
                .build();
    }

    @Bean
    public S3Presigner getS3esigner() {
        AwsCredentials basicAwsCredential = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(basicAwsCredential))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public AwsExternalServicePort awsExternalServicePort(S3AsyncClient getS3AsyncClient, S3Presigner getS3esigner) {
        return new AwsExternalServiceAdapter(getS3AsyncClient, getS3esigner);
    }

    @Bean
    public IAExternalServicePort iaExternalServicePort(WebClient webClient) {
        return new IAExternalServiceAdapter(webClient);
    }

}
