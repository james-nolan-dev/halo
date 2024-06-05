package me.nolanjames.halo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.ToDoubleBiFunction;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

    public ErrorDto handleGenericException(HttpServletRequest request, Exception exception) {
        // TODO: 05/06/2024 Avoid creating new List here
        LOGGER.error(exception.getMessage(), exception);
        List<String> errors = new ArrayList<>();
        errors.add(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ErrorDto(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errors,
                request.getServletPath()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        // TODO: 05/06/2024 Avoid creating new List here

        fieldErrors.forEach(fieldError -> {

        })

        new ErrorDto(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                (((ServletWebRequest) request).getRequest().getServletPath());
        )
    }
}
