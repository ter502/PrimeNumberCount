/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primeclient;

/**
 *
 * @author eiwte
 */
import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Main {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/TempQueue")
    private static Queue queue;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MessageProducer replyProducer = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        TextMessage message = null;
        TextListener listener = null;
        
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(queue);
            listener = new TextListener(session);
            consumer.setMessageListener(listener);
            replyProducer = session.createProducer(null);
            connection.start();
            String ch = "";
            Scanner inp = new Scanner(System.in);
            while(true) {
                System.out.println("Enter two numbers. Use ',' to seperate each number. TO end the program press enter");
                ch = inp.nextLine();
                message = session.createTextMessage();
                message.setText(ch);
                message.setJMSDestination(listener.getReplyDest());
                message.setJMSCorrelationID(listener.getCorrelationId());
                System.out.println("Sending message: " + message.getText());
                replyProducer.send(listener.getReplyDest(), message);
                if (ch.equals("")) {
                    break;
                }
            }
            
            
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
     
    }
    
}
