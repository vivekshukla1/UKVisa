package com.ilinksolutions.UKVisaDb.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
    public EntityNotFoundException(String error) {
    	super(error);
    }
}
