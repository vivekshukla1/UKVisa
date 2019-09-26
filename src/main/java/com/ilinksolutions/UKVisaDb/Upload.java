package com.ilinksolutions.UKVisaDb;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.UKVisaDb.utils.AES256Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Upload extends HttpServlet
{
	String message = null;
    Logger logger = LoggerFactory.getLogger(MainServlet.class);
	
    private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
    	try
    	{
    		logger.info("MainServlet: doPost: Begin.");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if(isMultipart)
            {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext())
                {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    logger.info("File field " + name + " with file name " + item.getName() + " detected.");
                    String unmarshalledString = convert(stream, Charset.defaultCharset());
                    message = AES256Manager.decryptMessage(unmarshalledString);
                    logger.info("AES256Manager: message decrypted: " + message);
                    request.setAttribute("message", message);
                    response.setHeader("message", String.valueOf(message));
                }                   	
            }
            else
            {
            	logger.info("Not multipart request.");
            }
        } 
        catch (FileUploadException ex)
        {
            logger.info("Upload: doPost: Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/index.jsp");
    	dispatcher.forward(request, response);
    }
    
    public String convert(InputStream inputStream, Charset charset) throws IOException
    {	 
    	StringBuilder stringBuilder = new StringBuilder();
    	String line = null;
    	
    	try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset)))
    	{	
    		while ((line = bufferedReader.readLine()) != null)
    		{
    			stringBuilder.append(line);
    		}
    	}
    	logger.info("MainServlet: doPost: End.");
    	return stringBuilder.toString();
    }
}