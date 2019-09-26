package com.ilinksolutions.UKVisaDb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailManager
{
	Logger logger = LoggerFactory.getLogger(EmailManager.class);
	private String text = null;
	private String subject = null;
	
	public EmailManager()
	{
	}

	public EmailManager(String subject, String text)
	{
		this.subject = subject;
		this.text = text;
	}
	
	public void send(String message)
	{
		logger.info("EmailManager: send: Begin.");
		try {
			final ByteArrayOutputStream document = createInMemoryDocument(message);
			if (document == null)
			{
				logger.warn("EmailManager: send: document: NULL.");
			}
			else
			{
				logger.info("EmailManager: send: document: NOT NULL.");
				final InputStream inputStream = new ByteArrayInputStream(document.toByteArray());
				final DataSource attachment = new ByteArrayDataSource(inputStream, "application/octet-stream");
				sendMimeMessageWithAttachments(attachment);
			}
		}
		catch (IOException | MailException | MessagingException e)
		{
			logger.warn("EmailManager: send: " + e.getMessage());
			logger.warn(e.getMessage(), e);
		}
		logger.info("EmailManager: send: End.");
	}

	private void sendMimeMessageWithAttachments(DataSource dataSource) throws MessagingException
	{
		logger.info("EmailManager: sendMimeMessageWithAttachments: Begin.");

		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	    javaMailSender.setHost("smtp.gmail.com");
	    javaMailSender.setPort(587);
	     
	    javaMailSender.setUsername("sungsam752729@gmail.com");
	    javaMailSender.setPassword("Idcams0!");
	     
	    Properties props = javaMailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject(subject);
		helper.setFrom("sungsam752729@gmail.com");
		helper.setTo("sungsam852729@gmail.com");
		helper.setReplyTo("sungsam852729@gmail.com");
		helper.setText(text, false);
		helper.addAttachment("message.eft", dataSource);
		javaMailSender.send(message);
		logger.info("EmailManager: sendMimeMessageWithAttachments: Sent Email!");
		logger.info("EmailManager: sendMimeMessageWithAttachments: End.");
	}

	private ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException {
		logger.info("EmailManager: createInMemoryDocument: Begin.");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(documentBody.getBytes());
		logger.info("EmailManager: createInMemoryDocument: End: " + outputStream);
		return outputStream;
	}
}