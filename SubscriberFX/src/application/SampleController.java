package application;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SampleController {
	@FXML
	TextArea textArea;

	private Subscriber subscriber;
	
	private String text = new String();

	@FXML
	private void initialize() {
		try {
			subscriber = new Subscriber("mq://localhost:7676", "ATJDurableTopic");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			subscriber.receiveQueueMessageAsync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class Subscriber {
		private JMSContext jmsContext;
		private JMSConsumer jmsConsumer;
		private Topic topic;

		Subscriber(String url, String topicName) throws JMSException {
			ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			jmsContext = connectionFactory.createContext();
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, url);
			topic = new com.sun.messaging.Topic(topicName); // "ATJTopic"
			jmsConsumer = jmsContext.createConsumer(topic);
		}

		public void receiveQueueMessageAsync() throws InterruptedException {
			jmsConsumer.setMessageListener(new AsynchSubscriber());
		}
	}

	public class AsynchSubscriber implements MessageListener {
		@Override
		public void onMessage(Message message) {
			if (message instanceof TextMessage)
				try {
					String messageText = ((TextMessage) message).getText();
					if(messageText != null) {
						text = text + messageText + "\n";
						textArea.setText(text);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
		}
	}
}
