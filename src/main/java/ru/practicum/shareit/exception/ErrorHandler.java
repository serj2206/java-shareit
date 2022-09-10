package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongParameterException(final WrongParameterException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Неверное значение");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Неверный запрос");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        log.warn("500 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Ошибка сервера");
    }
}