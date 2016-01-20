package com.cemic.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SettingsHelper
{
	private static Properties config = null;
	private static SettingsHelper theInstance = null;
	public static String address = null;

	public static SettingsHelper getInstance()
	{
		if(theInstance == null)
			theInstance = new SettingsHelper();
		return(theInstance);
	}

	private SettingsHelper()
	{
		super();
		if(config == null)
		{
			config = new Properties();
			InputStream input = null;
			/*try
			{
				String configFile = System.getProperty("cemic.configFile", "/opt/cemic/cfg/config.prp");
				input = new FileInputStream(configFile);
			}
			catch(Exception ex)
			{
			}*/
			try
			{
				if(input == null)
					input = getResourceStream(this, "/config.prp");
				if(input != null)
					config.load(input);
			}
			catch(Exception ex)
			{
				Logger.getLogger("Error").log(Level.INFO, "Error loading configuration", ex);
			}
			finally
			{
				if(input != null)
				{
					try
					{
						input.close();
					}
					catch(Exception ex)
					{
					}
				}
			}
			String serverUrl = System.getProperty("ServerUrl");
			if(serverUrl == null)
				serverUrl = config.getProperty("ServerUrl");
			if(serverUrl != null)
			{
				serverUrl = replace(serverUrl, "${ServerAddress}", getAddress());
				config.setProperty("ServerUrl", serverUrl);
			}
		}
	}
	
	public static InputStream getResourceStream(Object loader, String resourceName)
	{
		InputStream in = null;
		try
		{
			in = loader.getClass().getResourceAsStream(resourceName);
			if(in == null)
				return(null);
		}
		catch(Exception ex)
		{
			try
			{
				if(in != null)
					in.close();
			}
			catch(Exception e)
			{
			}
		}
		return(in);
	}

	public Properties getConfig()
	{
		return config;
	}
	
	public boolean isDebugMode()
	{
		return(config.getProperty("DEVMODE", "false").equals("true"));
	}
	
	public int getIntValue(String key, int defaultValue)
	{
		return(getIntValue(config, key, defaultValue, null));
	}
	
	public int getIntValue(Properties prp, String key, int defaultValue)
	{
		return(getIntValue(prp, key, defaultValue, null));
	}
	
	public int getIntValue(Properties prp, String key, int defaultValue, String loggerName)
	{
		int value = defaultValue;
		try
		{
			value = Integer.parseInt(prp.getProperty(key));
		}
		catch(Exception ex)
		{
			
		}
		if(loggerName != null)
			Logger.getLogger(loggerName).log(Level.DEBUG, "[" + key + "]=[" + value + "]", null);
		return(value);
	}
	
	public static long getLongValue(String strValue, long defaultValue)
	{
		long value = defaultValue;
		try
		{
			value = Long.parseLong(strValue);
		}
		catch(Exception ex)
		{
			
		}
		return(value);
	}
	
	public static Long getLongValue(String strValue, Long defaultValue)
	{
		Long value = defaultValue;
		try
		{
			value = new Long(strValue);
		}
		catch(Exception ex)
		{
			
		}
		return(value);
	}
	
	public String getProperty(Properties prp, String key, String defaultValue, String loggerName)
	{
		String value = prp.getProperty(key, defaultValue);
		if(loggerName != null)
			Logger.getLogger(loggerName).log(Level.DEBUG, "[" + key + "]=[" + value + "]", null);
		return(value);
	}
	
	public int getCurrentYear()
	{
		int currentYear = 0;
		if(isDebugMode())
			currentYear = getIntValue("currentYear", 0);
		if(currentYear == 0)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			currentYear = calendar.get(Calendar.YEAR);
		}
		return(currentYear);
	}
	
	public static String replace(String strSource, String strToFind, String strReplace)
	{
		if(strSource == null)
			return(strSource);
		int sep = 0;
		int lToFind = strToFind.length();
		int lReplace = strReplace.length();
		do
		{
			sep = strSource.indexOf(strToFind, sep);
			if(sep >= 0)
			{
				strSource = strSource.substring(0, sep) + strReplace + strSource.substring(sep + lToFind);
				sep += lReplace;
			}
		} while(sep >= 0);
		return(strSource);
	}
	
	public static String getAddress()
	{
		if(address == null)
		{
			try
			{
				InetAddress ia = InetAddress.getLocalHost();
				address = ia.getHostAddress();
			}
			catch(Exception e)
			{
			}
		}
		if(address == null)
			address = "";
		return(address);
	}
}