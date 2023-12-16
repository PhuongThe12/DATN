package luckystore.datn.controller;

import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.ExcelException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.exception.RestApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateExceptionHandler(DuplicateException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<?> nullExceptionHandler(NullException exception) {
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ExcelException.class)
    public ResponseEntity<?> nullExceptionHandler(ExcelException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<?> nullExceptionHandler(InvalidIdException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> nullExceptionHandler(NotFoundException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> nullExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> nullExceptionHandler(ConflictException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> restApiHandler(RestApiException exception) {
        return new ResponseEntity<>(exception.getData(), HttpStatus.BAD_REQUEST);
    }
}
