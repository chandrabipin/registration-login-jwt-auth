package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorResponseModel;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler{

	/**
	 * This is the method which handles all the exception and returns a generic method.
	 * @param ex
	 * @param request
	 * @return
	 */
	// This is must. This makes to handle all the exception. 
	// If we need to handle any specific exception then put only that exception in the value.
	// Same Exception type will go in the method as first argument as well.
/*	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
		return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
*/
	/**
	 * For custom error message
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
		ErrorResponseModel errorResonse = new ErrorResponseModel(new Date(), ex.getLocalizedMessage());
		return new ResponseEntity<>(errorResonse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle specific exception
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value= {NullPointerException.class})
	public ResponseEntity<Object> handleNPEException(NullPointerException ex, WebRequest request){
		ErrorResponseModel errorResonse = new ErrorResponseModel(new Date(), ex.getLocalizedMessage());
		return new ResponseEntity<>(errorResonse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Throw own custom exception
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value= {UserServiceException.class})
	public ResponseEntity<Object> handleCustomException(UserServiceException ex, WebRequest request){
		ErrorResponseModel errorResonse = new ErrorResponseModel(new Date(), ex.getLocalizedMessage());
		return new ResponseEntity<>(errorResonse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
