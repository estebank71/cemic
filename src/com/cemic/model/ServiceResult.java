package com.cemic.model;

import java.io.Serializable;

/**
 *
 * @author khe05617
 */
public class ServiceResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String result;
	private ServiceError error;

	public ServiceResult(String result, ServiceError error)
	{
		this.result = result;
		this.error = error;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public ServiceError getError()
	{
		return error;
	}

	public void setError(ServiceError error)
	{
		this.error = error;
	}
	
	@Override
	public String toString()
	{
		return "br.com.outclass.model.ServiceResult[ result=" + result + " ]";
	}
}
