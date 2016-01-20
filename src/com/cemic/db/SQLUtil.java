package com.cemic.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SQLUtil
{
	public static Double getDoubleValue(ResultSet rset, String columnName) throws SQLException
	{
		double value = rset.getDouble(columnName);
		if(rset.wasNull())
			return(null);
		return(new Double(value));
	}
	
	public static Double getDoubleValue(ResultSet rset, int columnIndex) throws SQLException
	{
		double value = rset.getDouble(columnIndex);
		if(rset.wasNull())
			return(null);
		return(new Double(value));
	}
	
	public static Long getLongValue(ResultSet rset, String columnName) throws SQLException
	{
		long value = rset.getLong(columnName);
		if(rset.wasNull())
			return(null);
		return(new Long(value));
	}
	
	public static Long setLong(PreparedStatement prepStmt, int parameterIndex, Long value) throws SQLException
	{
		if(value == null)
			prepStmt.setNull(parameterIndex, java.sql.Types.INTEGER);
		else
			prepStmt.setLong(parameterIndex, value.longValue());
		return(value);
	}
	
	public static Date setDate(PreparedStatement prepStmt, int parameterIndex, Date date) throws SQLException
	{
		if(date == null)
			prepStmt.setNull(parameterIndex, java.sql.Types.DATE);
		else
			prepStmt.setDate(parameterIndex, new java.sql.Date(date.getTime()));
		return(date);
	}
	
	public static Date setTimestamp(PreparedStatement prepStmt, int parameterIndex, Date date) throws SQLException
	{
		if(date == null)
			prepStmt.setNull(parameterIndex, java.sql.Types.DATE);
		else
			prepStmt.setTimestamp(parameterIndex, new java.sql.Timestamp(date.getTime()));
		return(date);
	}
}
