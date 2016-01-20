package com.cemic.model;

import java.util.Date;

public class User
{
	private String name;
	private String password;
	private Date lastAccess;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Date getLastAccess()
	{
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess)
	{
		this.lastAccess = lastAccess;
	}

	@Override
	public String toString()
	{
		return "User [name=" + name + ", password=" + password + ", lastAccess=" + lastAccess + "]";
	}
}
