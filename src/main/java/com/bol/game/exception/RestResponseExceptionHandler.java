package com.bol.game.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bol.game.controller.GameController;

@ControllerAdvice(assignableTypes = {GameController.class})
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	public RestResponseExceptionHandler() {
		super();
	}

	// 422
	@ExceptionHandler({ InvalidPlayerIdException.class })
	public ResponseEntity<Object> handleInvalidPlayerId(final Exception ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(),
				"The player Id must be 1 or 2");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ PlayerAlreadyActiveException.class })
	public ResponseEntity<Object> handlePlayerAlreadyActive(final Exception ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(),
				"Try with the oponent player");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ GameStateException.class })
	public ResponseEntity<Object> handleGameStateException(final Exception ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(),
				"The current state does not allow registration");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<?> constraintViolationExceptionHandler(final Exception ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(),
				"Please Select a valid value");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
