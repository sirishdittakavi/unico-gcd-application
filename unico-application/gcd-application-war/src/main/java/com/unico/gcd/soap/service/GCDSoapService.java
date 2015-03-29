package com.unico.gcd.soap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unico.gcd.jms.bean.JMSOperationsBean;
import com.unico.gcd.jms.dto.InputBean;

@Startup
@WebService
public class GCDSoapService {
	Logger logger = LogManager.getLogger(JMSOperationsBean.class);
	@EJB
	JMSOperationsBean operations;

	/**
	 * 
	 * @return
	 * @throws JMSException
	 */
	@WebMethod
	public Integer getGCD() {
		int gcd = 0;
		InputBean input = null;
		try {
			input = operations.getNextFromQueue();

		} catch (JMSException e) {
			// TODO Auto-generated catch block

			logger.error(e);
			throw new RuntimeException(
					"Sorry unable tp prcess your request now."
							+ "Please check whether queue is populated with correct input values");
		}
		if (input != null) {
			gcd = getGCD(input.getFirstNumber(), input.getSecondNumber());
		} else {
			throw new RuntimeException("Sorry No Messages availabe in queue");
		}
		return gcd;
	}

	@WebMethod
	public List<Integer> getGCDForAllElements() {

		List<Integer> returnList = new ArrayList<Integer>();

		InputBean inp;
		try {
			while ((inp = operations.getNextFromQueue()) != null) {
				returnList.add(getGCD(inp.getFirstNumber(),
						inp.getSecondNumber()));
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			throw new RuntimeException(
					"Sorry unable tp prcess your request now.Please check whther queue is populated with correct input values");

		}
		return returnList;
	}

	@WebMethod
	public int getSum() {
        logger.info("calculating sum od all GCD's");
		List<Integer> gcdList = getGCDForAllElements();
		int sum =0;
		for (Integer gcd : gcdList) {
			sum = sum + gcd;
		}
		    
		return sum;
	}
	
	private int getGCD(int gcdNum1, int gcdNum2) {

		return gcdNum2 == 0 ? gcdNum1 : getGCD(gcdNum2, gcdNum1 % gcdNum2);

	}
}
