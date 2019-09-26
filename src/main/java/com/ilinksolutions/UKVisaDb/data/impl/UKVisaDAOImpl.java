package com.ilinksolutions.UKVisaDb.data.impl;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;

import com.ilinksolutions.UKVisaDb.data.UKVisaDAO;
import com.ilinksolutions.UKVisaDb.domains.UKVisaMessage;

public class UKVisaDAOImpl implements UKVisaDAO
{
	Logger logger = LoggerFactory.getLogger(UKVisaDAOImpl.class);
	
	private final DataSource dataSource;

	public UKVisaDAOImpl()
	{
		dataSource = lookupDataSource();
	}

	public Connection getConnection() throws SQLException
	{
		return getDataSource().getConnection();
	}

	private DataSource getDataSource()
	{
		return dataSource;
	}
	
	private DataSource lookupDataSource()
	{
		Context initialContext = null;
		Context envContext = null;
		try
		{
			initialContext = new InitialContext();
			try
			{
				return (DataSource) initialContext.lookup(System.getenv("DB_JNDI"));
			}
			catch (NameNotFoundException e)
			{
				envContext = (Context) initialContext.lookup("java:comp/env");
				return (DataSource) envContext.lookup(System.getenv("DB_JNDI"));
			}
		}
		catch (NamingException e)
		{
			throw new RuntimeException("UKVisaDAOImpl: lookupDataSource: Could not look up datasource", e);
		}
	}

	@Override
	public UKVisaMessage save(UKVisaMessage message)
	{
		logger.info("UKVisaDAOImpl: save: Begin.");
		Connection connection = null;
		UKVisaMessage returnValue = null;
		PreparedStatement statement = null;
		String insertMessageSQL = "INSERT INTO public.visadata(person_id, first_name, last_name, contact_no, email) " + 
									"VALUES (?, ?, ?, ?, ?) RETURNING person_id";
		try
		{
			logger.info("UKVisaDAOImpl: save: " + message.toString());
			connection = getConnection();
			connection.setAutoCommit(true);
			statement = connection.prepareStatement(insertMessageSQL);
			statement.setInt(1, (int) message.getId());
			statement.setString(2, message.getFirstName());
			statement.setString(3, message.getLastName());
			statement.setString(4, message.getContactNo());
			statement.setString(5, message.getEmail());			
			ResultSet rs = statement.executeQuery();
			rs.next();
			returnValue = new UKVisaDAOImpl().getEntry((int)rs.getInt(1));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("UKVisaDAOImpl: save: SQLException: e: " + e.getMessage());
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: save: Could not communicate with DB.");
			}
		}
		logger.info("UKVisaDAOImpl: save: End.");
		return returnValue;
	}

	@Override
	public List<UKVisaMessage> list()
	{
		ResultSet rset = null;
		Statement statement = null;
		Connection connection = null;
		List<UKVisaMessage> list = null;
		try
		{
			connection = getConnection();
			statement = connection.createStatement();
			rset = statement.executeQuery("SELECT person_id, first_name, last_name, contact_no, email FROM visadata");
			list = new ArrayList<UKVisaMessage>();
			while (rset.next())
			{
				int id = rset.getInt(1);
				String firstName = rset.getString(2);
				String lastName = rset.getString(3);
				String contactNo = rset.getString(4);
				String email = rset.getString(5);
				list.add(new UKVisaMessage(id, firstName, lastName, contactNo, email));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("UKVisaDAOImpl: list: SQLException: e: " + e.getMessage());
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				rset.close();
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: list: Could not communicate with DB.");
			}
		}
		return list;
	}

	@Override
	public UKVisaMessage getEntry(int id)
	{
		logger.info("UKVisaDAOImpl: getEntry: Begin: " + id);
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connection = null;
		UKVisaMessage returnValue= null;
		String selectMessageSQL = "SELECT person_id, first_name, last_name, contact_no, email FROM visadata where person_id = ?";
		try
		{
			connection = getConnection();
			ps = connection.prepareStatement(selectMessageSQL);
		    ps.setInt(1, id);
		    rs = ps.executeQuery();
		    returnValue = new UKVisaMessage();
			while (rs.next())
			{
			    returnValue.setId(rs.getInt(1));
			    returnValue.setFirstName(rs.getString(2));
			    returnValue.setLastName(rs.getString(3));
			    returnValue.setContactNo(rs.getString(4));
			    returnValue.setEmail(rs.getString(5));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("UKVisaDAOImpl: getEntry: SQLException: e: " + e.getMessage());
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				rs.close();
				ps.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: getEntry: Could not communicate with DB.");
			}
		}
		logger.info("UKVisaDAOImpl: getEntry: End: " + returnValue.toString());
		return returnValue;
	}

	@Override
	public UKVisaMessage updateEntry(int id, UKVisaMessage message)
	{
		logger.info("UKVisaDAOImpl: updateEntry: Begin: " + id);
		PreparedStatement ps = null;
		Connection connection = null;
		UKVisaMessage returnValue= null;
		String updateMessageSQL = "update visadata set first_name = ?, last_name = ?, contact_no = ?, email = ? WHERE person_id = ?";

		try
		{
			connection = getConnection();
			connection.setAutoCommit(true);
			ps = connection.prepareStatement(updateMessageSQL);
		    ps.setString(1, message.getFirstName());
		    ps.setString(2, message.getLastName());
		    ps.setString(3, message.getContactNo());
		    ps.setString(4, message.getEmail());
		    ps.setInt(5, id);
		    ps.executeUpdate();
		    returnValue = new UKVisaDAOImpl().getEntry(id);
		    logger.info("UKVisaDAOImpl: updateEntry: returnValue: " + returnValue.toString());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("UKVisaDAOImpl: updateEntry: SQLException: e: " + e.getMessage());
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				ps.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: updateEntry: Could not communicate with DB.");
			}
		}
		logger.info("UKVisaDAOImpl: updateEntry: End: " + returnValue.toString());
		return returnValue;
	}
}
