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
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class MainServlet extends HttpServlet
{
	String message = null;
    Logger logger = LoggerFactory.getLogger(MainServlet.class);
	
    private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getServletContext().getResourceAsStream("/WEB-INF/index.html"), "UTF-8"));
		try
		{
			String line;
			boolean insideLoop = false;
			StringBuilder sb = new StringBuilder();
			sb.append("This is a test.");
			while ((line = reader.readLine()) != null)
			{
				if (line.trim().equals("<!-- begin repeat for each entry -->"))
				{
					insideLoop = true;
				}
				else if (line.trim().equals("<!-- end repeat for each entry -->"))
				{
					insideLoop = false;
				}
				else if (insideLoop)
				{
					sb.append(line).append("\n");
				}
				else
				{
					out.println(line);
				}
			}
		}
		finally
		{
			reader.close();
		}
	}
}
