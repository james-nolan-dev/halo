package me.nolanjames.halo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
import java.util.stream.Collectors;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

    public ErrorDto handleGenericException(HttpServletRequest request, Exception exception) {
        LOGGER.error(exception.getMessage(), exception);

        return new ErrorDto(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
                request.getServletPath()
        );

    }

    public ErrorDto handleUnauthorised(HttpServletRequest request, Exception exception) {
        LOGGER.error(exception.getMessage(), exception);

        return new ErrorDto(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                List.of(HttpStatus.UNAUTHORIZED.getReasonPhrase()),
                request.getServletPath()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);

        List<String> fieldErrors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(
                new ErrorDto(
                        new Date(),
                        HttpStatus.BAD_REQUEST.value(),
                        fieldErrors,
                        (((ServletWebRequest) request).getRequest().getServletPath())
                ),
                headers,
                status
        );
    }


}
