package com.cemic.model;

import java.util.Date;

public class Item
{
	private String name;
	private String lastName;
	private Date date;
	private String codeList;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getCodeList()
	{
		return codeList;
	}

	public void setCodeList(String codeList)
	{
		this.codeList = codeList;
	}
}
