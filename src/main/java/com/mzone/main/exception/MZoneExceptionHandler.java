package com.mzone.main.exception;

import io.sentry.Sentry;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MZoneExceptionHandler extends ResponseEntityExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(MZoneExceptionHandler.class);

    /**
     * Domain exceptions
     * {@link com.mzone.main.exception.DomainException}
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomainException(HttpServletRequest request,
                                                        HttpServletRequest response,
                                                        DomainException ex) {
        final String message = ex.getMessage();

        LOGGER.warn("DomainException", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.emptyFields(message, message));
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(HttpServletRequest request,
                                                                   HttpServletRequest response,
                                                                   JwtAuthenticationException ex) {
        final String message = ex.getMessage();

        LOGGER.warn(message, ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessage.emptyFields(message, message));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartException(HttpServletRequest request,
                                                           HttpServletRequest response,
                                                           MultipartException ex) {
        final String message = HttpStatus.BAD_REQUEST.toString();

        LOGGER.warn(message, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.emptyFields(message, message));
    }

    /**
     * Validation exceptions
     * {@link org.springframework.web.bind.MethodArgumentNotValidException}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        final Collection<FieldErrorInfo> fieldsErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(it -> new FieldErrorInfo(
                        it.getField(),
                        it.getRejectedValue(),
                        it.getDefaultMessage(),
                        it.getDefaultMessage()
                )).collect(Collectors.toList());

        final String message = "Bad request";
        return ResponseEntity.status(status)
                .headers(headers)
                .body(new ErrorMessage(message, message, fieldsErrors));
    }

    /**
     * All another exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest request,
                                                  HttpServletRequest response,
                                                  Exception ex) {
        final String message = "Oops, something went wrong...";

        LOGGER.error(message, ex);
        Sentry.captureException(ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.emptyFields(message, message));
    }

    @Data
    public static class FieldErrorInfo {
        private final String fieldName;
        private final Object rejectedValue;
        private final String message;
        private final String localizedMessage;
    }

    @Data
    public static class ErrorMessage {
        private final String message;
        private final String localizedMessage;
        private final Collection<FieldErrorInfo> fields;

        public static ErrorMessage emptyFields(String message, String localizedMessage) {
            return new ErrorMessage(message, localizedMessage, Collections.emptyList());
        }
    }

}
