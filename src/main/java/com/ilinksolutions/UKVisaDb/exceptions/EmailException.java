package com.ilinksolutions.UKVisaDb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class EmailException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EmailException(String error) {
		super(error); 
	}

}
