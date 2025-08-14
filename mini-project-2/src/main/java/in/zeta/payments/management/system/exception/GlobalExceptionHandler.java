package in.zeta.payments.management.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String TIME_STAMP  = "timestamp";

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException userAlreadyExistsException)
    {
        Map<String, Object> response  =  new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put( MESSAGE, userAlreadyExistsException.getMessage());
        response.put(STATUS, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException userNotFoundException)
    {
        Map<String, Object> response  =  new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put( MESSAGE, userNotFoundException.getMessage());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Object> handlePaymentNotFound(PaymentNotFoundException paymentNotFoundException)
    {
        Map<String, Object> response  =  new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put( MESSAGE, paymentNotFoundException.getMessage());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Object> handleAuthenticationFailed(AuthenticationFailedException authenticationFailedException)
    {
        Map<String, Object> response  =  new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put( MESSAGE, authenticationFailedException.getMessage());
        response.put(STATUS, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put(MESSAGE, "An unexpected error occurred " + exception.getMessage());
        response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put(MESSAGE, "Access denied: " + accessDeniedException.getMessage());
        response.put(STATUS, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PdfReportGenerationException.class)
    public ResponseEntity<Object> handlePdfReportGenerationException(PdfReportGenerationException pdfReportGenerationException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        response.put(MESSAGE, pdfReportGenerationException.getMessage());
        response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIME_STAMP, LocalDateTime.now());
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        response.put(MESSAGE, errorMessage);
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
