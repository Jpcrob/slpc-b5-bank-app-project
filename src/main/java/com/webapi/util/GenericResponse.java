package com.webapi.util;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> GenericResponse<T> empty() {
        return success(null, null);
    }

    public static <T> GenericResponse<T> success(T data, String message) {
        return GenericResponse.<T>builder()
                .message("SUCCESS!")
                .data(data)
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> error(String errorMessage) {
        return GenericResponse.<T>builder()
                .message(errorMessage)
                .success(false)
                .build();
    }
}