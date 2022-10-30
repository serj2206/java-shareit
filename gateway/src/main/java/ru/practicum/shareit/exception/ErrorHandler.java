package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Значение не найдено");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), "Неверный запрос");
    }



}