package com.ilinksolutions.UKVisaDb.rservices;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ilinksolutions.UKVisaDb.bservices.UKVisaService;
import com.ilinksolutions.UKVisaDb.domains.UKVisaMessage;
import com.ilinksolutions.UKVisaDb.exceptions.ErrorCode;
import com.ilinksolutions.UKVisaDb.exceptions.USCISException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="UK Visa DB Rest Test API", description="UK Visa DB Rest Test API", tags= {"UK Visa APIs"})
public class UKVisaDbRestController
{
	private static final String ID_SHOULD_BE_IN_NUMBER_FORMAT = "ID should be in number format!";
	private static final String MISSING_REQUIRED_FIELD_S = "Missing required field(s): ";
	private static final String FAILED_TO_UPDATE_THE_DATA_FOR_ID = "Failed to update the data for id: ";
	private static final String IS_NOT_A_VALID_EMAIL = " is not a valid Email.";
	private static final String FAILED_TO_SAVE_THE_DATA_FOR = "Failed to save the data for: ";
	private static final String ENTITY_NOT_FOUND_FOR_ID = "Entity not found for id: ";
	Logger logger = LoggerFactory.getLogger(UKVisaDbRestController.class);
	
	@ApiOperation(value="Returns UK Visa Service is Running...")
	@GetMapping("/serviceCheck")
	public String serviceCheck() {
		return "UK Visa Service is Running...";
	}
	
	@ApiOperation(value="Returns JSON Object with person information.")
    @GetMapping("/getmsg/{id}")
    public ResponseEntity<UKVisaMessage> readEntry(@PathVariable String id) 
    {
    	logger.info("P2RestController: readEntry: Begin.");
    	logger.info("P2RestController: readEntry: Path Variable: " + id);
    	if (isStringInt(id)) {
	    	try {
		        UKVisaService service = new UKVisaService();
		        UKVisaMessage returnValue = service.getEntry(new Integer(id).intValue());
		        if (returnValue == null || returnValue.getId() == 0)
		        {
		        	logger.info("P2RestController: readEntry: returnValue: NULL");
		            throw new USCISException(ENTITY_NOT_FOUND_FOR_ID +id, ErrorCode.DATABASE_ERROR_CODE);
		        }
		        else
		        {
		            logger.info("P2RestController: readEntry: returnValue: " + returnValue.toString());
		            return ResponseEntity.ok(returnValue);
		        }
	    	} catch (Exception e) {
	    		logger.error("P2RestController: readEntry: " + e);
	            throw new USCISException(ENTITY_NOT_FOUND_FOR_ID +id, ErrorCode.DATABASE_ERROR_CODE);
	    	}
      } else {
    	  throw new USCISException(ID_SHOULD_BE_IN_NUMBER_FORMAT, ErrorCode.BAD_REQUEST_ERROR_CODE);
      }
    }
    
	@ApiOperation(value="Save person informaiton and returns JSON object.")
    @PostMapping("/savemsg")
    public ResponseEntity<UKVisaMessage> registerMessage(@RequestBody UKVisaMessage message)
    {
    	logger.info("registerMessage: registerMessage: Begin.");
    	logger.info("registerMessage: registerMessage: Transform: " + message.toString());
    	if (message != null && (message.getId() == 0 || StringUtils.isBlank(message.getFirstName()) || StringUtils.isBlank(message.getLastName()))) {
    		getRequiredFields(message);
    	} else if (message != null && StringUtils.isNotBlank(message.getEmail()) && !isEmailValid(message.getEmail())){
    			throw new USCISException(message.getEmail()+ IS_NOT_A_VALID_EMAIL, ErrorCode.BAD_REQUEST_ERROR_CODE);
    		
    	}
    	try {
	    	UKVisaService service = new UKVisaService();
	    	UKVisaMessage returnValue = service.addEntry(message);
	    	if (returnValue == null)
	    	{
	    		logger.info("registerMessage: registerMessage: id: NULL.");
	    		 throw new USCISException(FAILED_TO_SAVE_THE_DATA_FOR + message.getFirstName(), ErrorCode.UNPROCESSABLE_ENTITY_ERROR_CODE);
	        }
	    	else
	    	{
	    		logger.info("registerMessage: registerMessage: id: End.");
	            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(returnValue.getId()).toUri();
	            return ResponseEntity.created(uri).body(returnValue);
	        }
    	} catch (Exception e) {
    		logger.error("P2RestController: registerMessage: " + e);
            throw new USCISException(FAILED_TO_SAVE_THE_DATA_FOR + message.getFirstName(), ErrorCode.UNPROCESSABLE_ENTITY_ERROR_CODE);
    	}
    }
    
	@ApiOperation(value="Update person informaiton and returns JSON object.")
	@PutMapping("/updatemsg/{id}")
    public ResponseEntity<UKVisaMessage> update(@RequestBody UKVisaMessage message, @PathVariable int id)
    {
    	String msg = (StringUtils.isBlank(message.getFirstName()) ? "firstName" : "");
		msg += (msg.length() >0 && StringUtils.isBlank(message.getLastName())? ", " : "") + (StringUtils.isBlank(message.getLastName()) ? "lastName" : "");
		
		if (msg.length() > 0) {
			logger.error("Following Required Fields are Missing: " + msg);
			throw new USCISException(MISSING_REQUIRED_FIELD_S+ msg, ErrorCode.BAD_REQUEST_ERROR_CODE);
		} else if (message != null && StringUtils.isNotBlank(message.getEmail()) && !isEmailValid(message.getEmail())){
			throw new USCISException(message.getEmail()+ IS_NOT_A_VALID_EMAIL, ErrorCode.BAD_REQUEST_ERROR_CODE);
		}
		
		try {
			UKVisaService service = new UKVisaService();
			UKVisaMessage returnValue = service.updateEntry(id, message);
	        if (returnValue == null)
	        {
	        	logger.error("P2RestController: Failed to update the data for id: "+id );
	        	throw new USCISException(FAILED_TO_UPDATE_THE_DATA_FOR_ID + id, ErrorCode.UNPROCESSABLE_ENTITY_ERROR_CODE);
	        }
	        else
	        {
	            return ResponseEntity.ok(returnValue);
	        }
        
		} catch (Exception e) {
    		logger.error("P2RestController: update: " + e);
    		throw new USCISException(FAILED_TO_UPDATE_THE_DATA_FOR_ID + id, ErrorCode.UNPROCESSABLE_ENTITY_ERROR_CODE);
    	}
    }
    

	private void getRequiredFields(UKVisaMessage message) {
		String msg = message.getId() == 0 ? " id" : "";
		msg += (msg.length() >0 ? ", " : "") + (StringUtils.isBlank(message.getFirstName()) ? " firstName" : "");
		msg += ((msg.length() >0 && StringUtils.isBlank(message.getLastName())) ? ", " : "") + (StringUtils.isBlank(message.getLastName()) ? " lastName" : "");
		logger.error("Following Required Fields are Missing: " + msg);
		throw new USCISException(MISSING_REQUIRED_FIELD_S+ msg, ErrorCode.BAD_REQUEST_ERROR_CODE);
	}
	
	private boolean isStringInt(String s)
	{
	    try
	    {
	        Integer.parseInt(s);
	        return true;
	    } catch (Exception ex)
	    {
	        return false;
	    }
	}
	
    private boolean isEmailValid(String email) {
	   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	   return email.matches(regex);
	}
}