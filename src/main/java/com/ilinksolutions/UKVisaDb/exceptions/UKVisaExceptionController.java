package com.ilinksolutions.UKVisaDb.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UKVisaExceptionController extends ResponseEntityExceptionHandler{
	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	public void notFoundException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.NOT_FOUND.value());
	}
	
	@ExceptionHandler(value = BadRequestException.class)
	public void requiredFieldsException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
	@ExceptionHandler(value = UnProcessableEntityException.class)
	public void saveDataException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
	
	
	
	@ExceptionHandler(value = EmailException.class)
	public void emailException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.EXPECTATION_FAILED.value());
	}
	
	
}
