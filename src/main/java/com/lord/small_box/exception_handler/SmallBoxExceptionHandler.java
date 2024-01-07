package com.lord.small_box.exception_handler;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.LoginException;

@ControllerAdvice
public class SmallBoxExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ItemNotFoundException.class)
	ResponseEntity<String> handleItemNotFound(ItemNotFoundException ex){
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<String> handleRuntime(RuntimeException ex){
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.EXPECTATION_FAILED);
	}
	
	
	@ExceptionHandler(LoginException.class)
	ResponseEntity<String> handleLogin(LoginException ex){
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
	}
	
	

}
