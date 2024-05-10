package com.lord.small_box.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.LoginException;
import com.lord.small_box.exceptions.MaxAmountExeededException;
import com.lord.small_box.exceptions.MaxRotationExceededException;
import com.lord.small_box.exceptions.PasswordInvalidException;
import com.lord.small_box.exceptions.TextFileInvalidException;

@ControllerAdvice
public class SmallBoxExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ItemNotFoundException.class)
	ResponseEntity<String> handleItemNotFound(ItemNotFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<String> handleRuntime(RuntimeException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(LoginException.class)
	ResponseEntity<String> handleLogin(LoginException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MaxRotationExceededException.class)
	ResponseEntity<String> handleMaxRotation(MaxRotationExceededException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(MaxAmountExeededException.class)
	ResponseEntity<String> handleMaxAmount(MaxAmountExeededException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(PasswordInvalidException.class)
	ResponseEntity<String> handlePasswordInvalid(PasswordInvalidException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(TextFileInvalidException.class)
	ResponseEntity<String> handleTextFileInvalid(TextFileInvalidException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
