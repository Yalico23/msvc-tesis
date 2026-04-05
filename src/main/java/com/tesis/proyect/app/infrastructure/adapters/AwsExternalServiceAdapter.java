package com.tesis.proyect.app.infrastructure.adapters;

import com.tesis.proyect.app.domain.ports.output.AwsExternalServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.nio.ByteBuffer;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsExternalServiceAdapter implements AwsExternalServicePort {

    private final S3AsyncClient s3AsyncClient;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public Mono<Boolean> uploadFile(String key, FilePart filePart) {
        return filePart.content()
                .collectList()
                .flatMap(dataBuffers -> {
                    try {
                        // Para archivos muy grandes (como videos de 20 minutos)
                        // es mejor usar multipart upload

                        long totalSize = dataBuffers.stream()
                                .mapToLong(DataBuffer::readableByteCount)
                                .sum();

                        log.info("Tamaño del archivo grande: {} MB", totalSize / (1024 * 1024));

                        // Convertir a bytes
                        byte[] allBytes = new byte[(int) totalSize];
                        int position = 0;

                        for (DataBuffer buffer : dataBuffers) {
                            byte[] bytes = new byte[buffer.readableByteCount()];
                            buffer.read(bytes);
                            System.arraycopy(bytes, 0, allBytes, position, bytes.length);
                            position += bytes.length;
                            DataBufferUtils.release(buffer);
                        }

                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .contentLength(totalSize)
                                .contentType("video/webp") // Tipo específico para tu video
                                .build();

                        AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(allBytes);

                        return Mono.fromFuture(() ->
                                s3AsyncClient.putObject(putObjectRequest, requestBody)
                        );

                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .map(response -> response.sdkHttpResponse().isSuccessful())
                .doOnSuccess(success -> log.info("Upload exitoso para archivo grande: {}", success))
                .onErrorResume(e -> {
                    log.error("Error en upload de archivo grande [{}]: {}", key, e.getMessage(), e);
                    return Mono.error(new RuntimeException("Error al subir archivo grande a S3", e));
                });
    }

    @Override
    public Mono<String> generatePresigned(String key, Duration duration) {
        return Mono.fromCallable(() -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest)
                    .url()
                    .toString();
        }).onErrorResume(e -> {
            log.error("Error generando URL presignada para [{}]: {}", key, e.getMessage(), e);
            return Mono.error(new RuntimeException("Error al generar URL presignada", e));
        });
    }


}
