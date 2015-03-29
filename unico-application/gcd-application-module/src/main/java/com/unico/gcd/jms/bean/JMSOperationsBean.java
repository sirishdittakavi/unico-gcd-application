/**
 * 
 */
package com.unico.gcd.jms.bean;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unico.gcd.jms.dto.InputBean;

/**
 * @author sirishdittakavi
 *
 */
@Singleton
@Startup
public class JMSOperationsBean {

	Logger logger = LogManager.getLogger(JMSOperationsBean.class);

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/jms/queue/test")
	private Queue queue;


	private Connection conn = null;
	private Session sess = null;
	private MessageProducer prod = null;
	private QueueBrowser browser = null;

	private final String USERNAME = "appadmin";
	private final String PASSWORD = "Siri@1508";

	@PostConstruct
	public void initialize() {

		try {
			conn = connectionFactory.createConnection(USERNAME, PASSWORD);
			sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			prod = sess.createProducer(queue);
			conn.start();

		} catch (JMSException e) {
			logger.error("Error in getting conn and session of GCD Queue");
			logger.error(e);
		}
	}

	/*
	 * receives
	 * 
	 * @param inp
	 */
	public void postToQueue(int gcdNum1, int gcdNum2) {

		try {
			InputBean inputBean = new InputBean();

			inputBean.setFirstNumber(gcdNum1);
			inputBean.setSecondNumber(gcdNum2);

			ObjectMessage messg;
			messg = sess.createObjectMessage(inputBean);
			prod.send(messg);
			logger.error("Sucessfully Added "+gcdNum1 + " and "+gcdNum2 +"to GCD Queue");

		} catch (JMSException e) {
			// TODO Auto-generated catch block

			logger.error(e);
			throw new RuntimeException(
					"Unable to add" +gcdNum1 + " and "+gcdNum2 +"to GCD Queue");

		}

	}

	public List<InputBean> getAllElementsInQueue() {

		ArrayList<InputBean> list = new ArrayList<InputBean>();

		try {
			browser = sess.createBrowser(queue);
			@SuppressWarnings("unchecked")
			Enumeration<Message> msgs = browser.getEnumeration();

			if (!msgs.hasMoreElements()) {
				
				logger.info("No messages in the GCD Queue");
			} else {
				while (msgs.hasMoreElements()) {
					ObjectMessage tempMsg = (ObjectMessage) msgs.nextElement();
					list.add((InputBean) tempMsg.getObject());
				}
				logger.info("Sucessfully processed gcdList Request");
				logger.debug("gcdList values" +list.toString());
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}

		// TODO Auto-generated method stub
		return list;
	}

	/**
	 * 
	 * @return
	 * @throws JMSException
	 */
	public InputBean getNextFromQueue() throws JMSException {

		MessageConsumer mc;
		ObjectMessage msg = null;
		mc = sess.createConsumer(queue);
		msg = (ObjectMessage) mc.receiveNoWait();
		mc.close();

		InputBean ret = null;
		if (msg != null) {
			ret = (InputBean) msg.getObject();
		}
		return ret;
	}
	
	@PreDestroy
	public void close() {
		logger.info("Closing JMS connections");
		
		try {
			sess.close();
			conn.close();
		} catch (JMSException e) {
			logger.warn("Unable to close cnnection");
		}
		
	}

	/**
	 * @param sess the sess to set
	 */
	public void setSess(Session sess) {
		this.sess = sess;
	}

	/**
	 * @param prod the prod to set
	 */
	public void setProd(MessageProducer prod) {
		this.prod = prod;
	}
	
	/**
	 * @param queue the queue to set
	 */
	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
}
