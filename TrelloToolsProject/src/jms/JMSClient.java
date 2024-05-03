package jms;



import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import entities.User;

@Named
@ApplicationScoped
public class JMSClient {
	
	
	@Inject
	JMSContext context;
	@Resource(mappedName="java:jboss/exported/jms/queue/MyTrelloQueue")
	private Queue MyTrelloQueue;

	
	public void sendMessage(String msg)
	{
		try{JMSProducer producer=context.createProducer();
		TextMessage message = context.createTextMessage(msg);
		producer.send((Destination) MyTrelloQueue, message);
		System.out.println(msg);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public String getMessage(String msg)
	{
		JMSConsumer consumer = context.createConsumer((Destination) MyTrelloQueue);

		try{
		TextMessage message = (TextMessage)consumer.receiveNoWait();
		if(message!=null)
		{
			System.out.println("message recieved:"+message);
			return message.getBody(String.class);
		}
		else
		{
			return null;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		finally
		{
			consumer.close();
		}
		
	}

}
