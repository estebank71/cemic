package com.cemic.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cemic.db.ConnectionManager;
import com.cemic.model.User;

public class UserService
{
	public User get(String name, String password)
	{
		User user = null;
		PreparedStatement prepStmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try
		{
			conn = ConnectionManager.getConnection(true);
			prepStmt = conn.prepareStatement("select * from tb_user where user_name = ? and user_password = ?");
			prepStmt.setString(1, name);
			prepStmt.setString(2, password);
			rset = prepStmt.executeQuery();
			if(rset != null && rset.next())
			{
				user = read(rset);
				if(user != null)
				{
					ConnectionManager.cleanUp(rset, prepStmt, null);
					rset = null;
					prepStmt = null;
					prepStmt = conn.prepareStatement("update tb_user set last_access = now() where user_name = ?");
					prepStmt.setString(1, name);
					prepStmt.executeUpdate();
				}
			}
		}
		catch(SQLException ex)
		{
			Logger.getLogger("Error").log(Level.ERROR, "name=[" + name + "]", ex);
		}
		finally
		{
			ConnectionManager.cleanUp(rset, prepStmt, conn);
		}
		return(user);
	}
	
	private User read(ResultSet rset) throws SQLException
	{
		User user = new User();
		user.setName(rset.getString("user_name"));
		user.setLastAccess(rset.getDate("last_access"));
		return(user);
	}
}
