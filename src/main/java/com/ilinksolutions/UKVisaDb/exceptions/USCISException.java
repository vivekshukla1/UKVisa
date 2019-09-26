package com.ilinksolutions.UKVisaDb.exceptions;

import java.util.List;
import java.util.ArrayList;

public class USCISException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private ErrorCode code = null;
		private List<Throwable> exceptions = null; 

		public USCISException(ErrorCode code)
		{
			super();
			this.code = code;
		}

		public USCISException(String message, ErrorCode code)
		{
			super(message);
			this.code = code;
		    if (code.equals(ErrorCode.BAD_REQUEST_ERROR_CODE)) {
		    	throw new BadRequestException(message);
		    } else if (code.equals(ErrorCode.ENTITY_NOT_FOUND_ERROR_CODE)){
		    	throw new EntityNotFoundException(message);
		    } else if (code.equals(ErrorCode.UNPROCESSABLE_ENTITY_ERROR_CODE)){
		    	throw new UnProcessableEntityException(message);
		    } else if (code.equals(ErrorCode.EMAIL_ERROR_CODE)){
		    	throw new EmailException(message);
		    }
		}

		public USCISException(Throwable cause, ErrorCode code)
		{
			super(cause);
			this.code = code;
		}
		
		public USCISException(String message, Throwable cause, ErrorCode code)
		{
			super(message, cause);
			this.code = code;
		}

		public ErrorCode getCode()
		{
			return this.code;
		}

		public List<Throwable> getExceptions()
		{
			return exceptions;
		}

		public void setExceptions(List<Throwable> _x)
		{
			this.exceptions = _x;
		}
		
		public void addException(Throwable _x)
		{
			if(exceptions == null)
			{
				exceptions = new ArrayList<Throwable>();
				exceptions.add(_x);
			}
			else
			{
				exceptions.add(_x);
			}
		}
}
