package com.cemic.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import com.cemic.helper.SettingsHelper;

public class ConnectionPool
{
	private DataSource datasource = null;
	private String databaseName = null;
	
	public ConnectionPool()
	{
	}
	
	public void finalize() throws Throwable
	{
		super.finalize();
	}
	
	public void init(String url, String driver, String userName, String password)
	{
		String[] parts = url.split("/");
		String[] urlParts = parts[2].split(":");
		String serverName = urlParts[0];
		String portNumber = urlParts[1];
		databaseName = parts[3];
		
		datasource = new MysqlDataSource();
		MysqlDataSource mysqlDataSource = (MysqlDataSource)datasource;
		mysqlDataSource.setUser(userName);
		mysqlDataSource.setPassword(password);
		mysqlDataSource.setDatabaseName(databaseName);
		mysqlDataSource.setServerName(serverName);
		mysqlDataSource.setPortNumber(SettingsHelper.getInstance().getIntValue(portNumber, 3306));
	}
	
	public Connection getConnection() throws SQLException
	{
		try
		{
			Connection conn = datasource.getConnection();
			return(conn);
		}
		catch(Exception ex)
		{
			Logger.getLogger("Error").log(Level.ERROR, datasource.toString(), ex);
		}
		return(null);
	}
	
	public static String stackTraceToString(Throwable ext)
	{
		String strBuff = null;
		try
		{
			java.io.ByteArrayOutputStream buff = new java.io.ByteArrayOutputStream(256);
			java.io.PrintStream printStream = new java.io.PrintStream(buff);
			ext.printStackTrace(printStream);
			strBuff = buff.toString();
		}
		catch(Exception ex)
		{
		}
		return(strBuff);
	}
}