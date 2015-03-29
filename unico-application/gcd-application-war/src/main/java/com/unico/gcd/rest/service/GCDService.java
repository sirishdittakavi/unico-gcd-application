package com.unico.gcd.rest.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unico.gcd.jms.bean.JMSOperationsBean;
import com.unico.gcd.jms.dto.InputBean;

@Stateless
@Path("/RestService")
public class GCDService {
	Logger logger = LogManager.getLogger(JMSOperationsBean.class);
	@Inject JMSOperationsBean operations;
	@GET
	@Path("/push")
	public Response push(@QueryParam("id1") int firstNum,
							 @QueryParam("id2") int secondNum) {
		logger.info("Adding "+firstNum + " and "+secondNum +" to GCD Queue ...");
		operations.postToQueue(firstNum, secondNum);
		return Response.status(200).entity("OK").build();
	}

	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_JSON})
	public Response list() {
		logger.info("Preparing List of ids added to GCD Queue");
		List<InputBean> list = operations.getAllElementsInQueue();
		return Response.status(200).entity(list).build();
	}
}
