package com.cemic.resource;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import com.cemic.model.ServiceError;
import com.cemic.model.ServiceResult;
import com.cemic.model.User;

public class Login
{
	@GET
	public Response login()
	{
		User user = null;
		if(user != null)
			return Response.ok(user).build();
		return Response.ok(new ServiceResult("fail", new ServiceError("100", "User not found"))).build();
	}
}
