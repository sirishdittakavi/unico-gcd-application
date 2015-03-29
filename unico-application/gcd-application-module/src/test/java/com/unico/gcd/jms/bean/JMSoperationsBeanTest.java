/**
 * 
 */
package com.unico.gcd.jms.bean;

import static org.easymock.EasyMock.expect;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Test;

import com.unico.gcd.jms.dto.InputBean;

/**
 * @author sirishdittakavi
 *
 */
public class JMSoperationsBeanTest extends TestCase {

	private Session mockSession;

	private MessageProducer mockProducer;
	
	private Queue mockQueue;
	
	private MessageConsumer mc;

	JMSOperationsBean opBean = null;

	public JMSoperationsBeanTest() {
		mockSession = EasyMock.createMock(Session.class);
		mockProducer = EasyMock.createMock(MessageProducer.class);
		mockQueue = EasyMock.createMock(Queue.class);
		opBean = new JMSOperationsBean();

	}

	@Test
	public void testPostToQueue() throws Exception {
		opBean.setSess(mockSession);
		opBean.setProd(mockProducer);

		InputBean inputBean = new InputBean();

		inputBean.setFirstNumber(2);
		inputBean.setSecondNumber(10);

		ObjectMessage messg = EasyMock.createMock(ObjectMessage.class);

		expect(mockSession.createObjectMessage(inputBean)).andReturn(messg);

		EasyMock.replay(mockSession);

		opBean.postToQueue(inputBean.getFirstNumber(),
				inputBean.getSecondNumber());

	}
	
	@Test
	public void testPostToQueueThrowsException() throws JMSException {
		opBean.setSess(mockSession);
		opBean.setProd(mockProducer);

		InputBean inputBean = new InputBean();

		inputBean.setFirstNumber(2);
		inputBean.setSecondNumber(10);

		ObjectMessage messg = EasyMock.createMock(ObjectMessage.class);

		expect(mockSession.createObjectMessage(inputBean)).andThrow(new JMSException(""));

		EasyMock.replay(mockSession);

		try {
			opBean.postToQueue(inputBean.getFirstNumber(),
					inputBean.getSecondNumber());
			fail("Expecting a runtime exception");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testGetNextFromQueue() throws Exception {
		opBean.setSess(mockSession);
		opBean.setProd(mockProducer);

		InputBean inputBean = new InputBean();

		inputBean.setFirstNumber(2);
		inputBean.setSecondNumber(10);

		ObjectMessage messg = EasyMock.createMock(ObjectMessage.class);
		
		mc = EasyMock.createMock(MessageConsumer.class);

		expect(mockSession.createConsumer(null)).andReturn(mc);
		expect(mc.receiveNoWait()).andReturn(messg);
		mc.close();
		EasyMock.expectLastCall();

		EasyMock.replay(mockSession);
		EasyMock.replay(mc);
		
		
		opBean.getNextFromQueue();

	}

}
