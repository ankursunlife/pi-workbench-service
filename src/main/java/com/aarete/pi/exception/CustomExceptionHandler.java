package com.aarete.pi.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author RK
 */
@RestControllerAdvice
@Validated
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
    public final ResponseEntity<Object> exception(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(value = RecordNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public final ResponseEntity<Object> exception(RecordNotFound ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Not Found", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = WorkbenchException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public final ResponseEntity<Object> exception(WorkbenchException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Something went wrong", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(value = BadRequestException.class)
    public final ResponseEntity<Object> exception(BadRequestException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Request not matching", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = AccessDenied.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public final ResponseEntity<Object> exception(AccessDenied ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("User is not configured", details);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
	
}
