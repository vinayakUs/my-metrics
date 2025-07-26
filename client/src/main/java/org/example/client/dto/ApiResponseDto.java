package org.example.client.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

/*
 * A generic API response DTO.
 * This class can be extended to create specific response DTOs for different API
 * endpoints.
 */
public class ApiResponseDto<T> {
    private  T data;
    private  boolean success;
    private  String path;
    private  String cause;
    private  String timestamp;

    public ApiResponseDto(){

    }

    public ApiResponseDto(boolean success, T data) {
        this.timestamp = Instant.now().toString();
        this.success = success;
        this.data = data;
        this.path = null;
        this.cause = null;
    }

    public ApiResponseDto(boolean success, T data, String cause, String path) {
        this.timestamp = Instant.now().toString();
        this.success = success;
        this.data = data;
        this.path = path;
        this.cause = cause;
    }

    public T getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCause() {
        return cause;
    }

    public String getPath() {
        return path;
    }
}