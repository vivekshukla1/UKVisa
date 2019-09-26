package com.ilinksolutions.UKVisaDb.utils;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES256Manager
{
	private static Logger logger = LoggerFactory.getLogger(AES256Manager.class);
	
	private static String secretKey = "iLink0!0!kniLi";
	private static String salt = "0!kniLiiLink0!";
	
	public static String decrypt(String strToDecrypt, String secret) {
	    try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	    }
	    catch (Exception e) {
	    	logger.error("Error while decrypting: " + e.toString());
	    }
	    return null;
	}
	
	public static String encrypt(String strToEncrypt, String secret) 
	{
	    try 
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	        
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	        
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    } 
	    catch (Exception e) 
	    {
	    	logger.error("Error while encrypting: " + e.toString());
	    }
	    return null;
	}
	
	public static String encryptMessage(String message)
	{
		logger.info("AES256Manager: encryptMessage: Begin.");
	    String encryptedString = AES256Manager.encrypt(message, secretKey);
	    logger.info("AES256Manager: encryptMessage: message: " + message);
	    logger.info("AES256Manager: encryptMessage: message: " + encryptedString);
	    logger.info("AES256Manager: encryptMessage: End.");
	    return encryptedString;
	}
	
	public static String decryptMessage(String message)
	{
		logger.info("AES256Manager: decryptMessage: Begin.");
	    String decryptedString = AES256Manager.decrypt(message, secretKey);
	    logger.info("AES256Manager: decryptMessage: message: " + message);
	    logger.info("AES256Manager: decryptMessage: message: " + decryptedString);
	    logger.info("AES256Manager: decryptMessage: End.");
	    return decryptedString;
	}
}