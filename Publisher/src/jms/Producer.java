package jms;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Producer
 */
@WebServlet(description = "Servlet P2P producer", urlPatterns = { "/Producer" })
public class Producer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(mappedName = "jms/ATJDurableConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/ATJDurableTopic")
	private Topic topic;

	/**
	 * Default constructor.
	 */
	public Producer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		JMSContext jmsContext = connectionFactory.createContext();
		JMSProducer jmsProducer = jmsContext.createProducer();
		for (int i = 0; i < 10; i++) {
			String msg = "Message_" + i;
			jmsProducer.send(topic, msg);
			response.getWriter().printf("<p> '%s' was sent. </p>", msg);
			// response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}
}
