package com.erasm.exception;

import com.erasm.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({UserNotFoundException.class, EmployeeNotFoundException.class, ProjectNotFoundException.class, SkillNotFoundException.class, ResourceRequestNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        logger.warn("Not found: {}", ex.getMessage());
        String code = ex instanceof UserNotFoundException ? "USER_NOT_FOUND" : ex instanceof EmployeeNotFoundException ? "EMPLOYEE_NOT_FOUND" : ex instanceof ProjectNotFoundException ? "PROJECT_NOT_FOUND" : ex instanceof SkillNotFoundException ? "SKILL_NOT_FOUND" : "REQUEST_NOT_FOUND";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(), code));
    }

    @ExceptionHandler(AllocationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAllocation(AllocationException ex) { logger.warn("Allocation error: {}", ex.getMessage()); return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage(), "ALLOCATION_ERROR")); }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTransition(InvalidStatusTransitionException ex) { return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage(), "INVALID_STATUS_TRANSITION")); }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage(), "DUPLICATE_RESOURCE")); }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiResponse<Void>> handleLocked(AccountLockedException ex) { return ResponseEntity.status(HttpStatus.LOCKED).body(ApiResponse.error(ex.getMessage(), "ACCOUNT_LOCKED")); }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauth(UnauthorizedAccessException ex) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(ex.getMessage(), "UNAUTHORIZED")); }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Access denied", "FORBIDDEN")); }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthenticationException ex) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Authentication failed", "AUTH_FAILED")); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError e : ex.getBindingResult().getFieldErrors()) errors.put(e.getField(), e.getDefaultMessage());
        return ResponseEntity.badRequest().body(ApiResponse.validationError("Validation failed", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> errors.put(cv.getPropertyPath().toString(), cv.getMessage()));
        return ResponseEntity.badRequest().body(ApiResponse.validationError("Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) { logger.error("Unexpected error: {}", ex.getMessage(), ex); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR")); }
}
