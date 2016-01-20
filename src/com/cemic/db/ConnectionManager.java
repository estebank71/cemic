package com.cemic.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ConnectionManager
{
	private static Hashtable<String, ConnectionPool> htConnectionPool = new Hashtable<String, ConnectionPool>();
	private static Hashtable<String, Hashtable<String, Connection>> htThreadConnection = new Hashtable<String, Hashtable<String, Connection>>();
	private static ConnectionManager theInstance = null;
	private static Logger logger = Logger.getLogger("DB");
	private static long openCount = 0;
	private static long closeCount = 0;
	private static String defaultPoolKey = "DEFAULT";
	
	private ConnectionManager()
	{	
	}
	
	public ConnectionManager getInstance()
	{
		if(theInstance == null)
			theInstance = new ConnectionManager();
		return(theInstance);
	}
	
	public static int releaseThreadConnections()
	{
		int count = 0;
		String threadKey = Thread.currentThread().getName();
		Hashtable<String, Connection> connectionList = htThreadConnection.get(threadKey);
		if(connectionList != null)
		{
			Iterator<String> it = connectionList.keySet().iterator();
			while(it.hasNext())
			{
				String poolKey = it.next();
				cleanUp(null, null, connectionList.get(poolKey));
			}
		}
		htThreadConnection.remove(threadKey);
		return(count);
	}
	
	private static Connection getThreadConnection(String threadKey, String poolKey)
	{
		Hashtable<String, Connection> connectionList = htThreadConnection.get(threadKey);
		Connection conn = null;
		if(connectionList != null)
			conn = connectionList.get(poolKey);
		return(conn);
	}
	
	private static Connection setThreadConnection(String threadKey, String poolKey, Connection conn)
	{
		if(conn != null)
		{
			Hashtable<String, Connection> connectionList = htThreadConnection.get(threadKey);
			if(connectionList == null)
				connectionList = new Hashtable<String, Connection>();
			connectionList.put(poolKey, conn);
			htThreadConnection.put(threadKey, connectionList);
		}
		return(conn);
	}
	
	public static Connection getConnection() throws SQLException
	{
		return(getConnection(true));
	}
	
	public static Connection getConnection(boolean threadConnection) throws SQLException
	{
		String threadKey = Thread.currentThread().getName();
		Connection conn = null;
		if(threadConnection)
			conn = getThreadConnection(threadKey, defaultPoolKey);
		if(conn == null)
		{
			ConnectionPool connectionPool = htConnectionPool.get(defaultPoolKey);
			if(connectionPool == null)
			{
				Properties systemProperties = System.getProperties();
				String jdbcConnectionString = systemProperties.getProperty("JDBC_CONNECTION_STRING", "jdbc:mysql://127.0.0.1:3606/cemic?user=cemicappl&password=369emc20");
				String[] parts = jdbcConnectionString.split("\\?|&");
				String url = parts[0];
				String user = null;
				String password = null;
				if(parts.length > 1)
				{
					String[] tmp = parts[1].split("=");
					if(tmp.length > 1)
						user = tmp[1];
				}
				if(user == null)
					user = systemProperties.getProperty("JDBCUserName", "cemicappl");
				if(parts.length > 2)
				{
					String[] tmp = parts[2].split("=");
					if(tmp.length > 1)
						password = tmp[1];
				}
				if(password == null)
					password = systemProperties.getProperty("JDBCPassword", "369emc20");
				connectionPool = createConnectionPool(defaultPoolKey, url, user, password);
			}
			conn = connectionPool.getConnection();
			if(threadConnection)
				setThreadConnection(threadKey, defaultPoolKey, conn);
		}
		//logger.log(Level.INFO, "getConnection[" + conn + "]");
		if(conn != null)
			conn.setAutoCommit(false);
		return(conn);
	}
	
	public static ConnectionPool createConnectionPool(String poolKey, String url, String user, String password) throws SQLException
	{
		ConnectionPool connectionPool = new ConnectionPool();
		try
		{
			Properties systemProperties = System.getProperties();
			String driver = systemProperties.getProperty(poolKey + "_JDBCDriver", "com.mysql.jdbc.Driver");
			logger.log(Level.INFO, "Init connection pool: driver=[" + driver + "]url=[" + url + "]user=[" + user + "]");
			connectionPool.init(url, driver, user, password);
			logger.log(Level.INFO, "OK Init");
			htConnectionPool.put(poolKey, connectionPool);
		}
		catch(Exception ex)
		{
			Logger.getLogger("Error").log(Level.ERROR, "poolKey=[" + poolKey + "]", ex);
		}
		return(connectionPool);
	}
	
	public static void cleanUp(ResultSet rset, Statement stmt, Connection conn)
	{
		if(rset != null)
		{
			try
			{
				rset.close();
			}
			catch(Exception ex)
			{
			}
		}
		if(stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch(Exception ex)
			{
			}
		}
		if(conn != null)
		{
			try
			{
				//logger.log(Level.INFO, "conn.close[" + conn + "]");
				conn.close();
				incCloseCount();
			}
			catch(Exception ex)
			{
			}
		}
	}
	
	public synchronized static void incOpenCount()
	{
		openCount++;
	}
	
	public synchronized static void incCloseCount()
	{
		closeCount++;
	}
	
	public static String getCounts()
	{
		return("[" + openCount + "][" + closeCount + "][" + (openCount - closeCount) + "]");
	}
}