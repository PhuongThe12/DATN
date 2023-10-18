package luckystore.datn.controller;

import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity duplicateExceptionHandler(DuplicateException exception) {
        return new ResponseEntity(exception.getData(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity nullExceptionHandler(NullException exception) {
        return new ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity nullExceptionHandler(NotFoundException exception) {
        return new ResponseEntity(exception.getData(), HttpStatus.NOT_FOUND);
    }
}
