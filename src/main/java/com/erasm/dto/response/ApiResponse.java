package com.erasm.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success; private String message; private T data;
    @Builder.Default private LocalDateTime timestamp = LocalDateTime.now();
    private String errorCode; private Map<String, String> errors;

    public static <T> ApiResponse<T> success(String message, T data) { return ApiResponse.<T>builder().success(true).message(message).data(data).timestamp(LocalDateTime.now()).build(); }
    public static <T> ApiResponse<T> success(T data) { return success("Success", data); }
    public static <T> ApiResponse<T> error(String message, String errorCode) { return ApiResponse.<T>builder().success(false).message(message).errorCode(errorCode).timestamp(LocalDateTime.now()).build(); }
    public static <T> ApiResponse<T> validationError(String message, Map<String, String> errors) { return ApiResponse.<T>builder().success(false).message(message).errorCode("VALIDATION_ERROR").errors(errors).timestamp(LocalDateTime.now()).build(); }
}
