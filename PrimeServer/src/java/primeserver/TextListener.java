/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primeserver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.Session;
import javax.jms.Destination;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    private MessageProducer producer;
    private Session session;
    private Destination tempDest;
    
    public TextListener(Session session, MessageProducer producer, Destination tempDest) {
        this.session = session;
        this.producer = producer;
        this.tempDest = tempDest;
    }
    
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());
                
                //parse to int
                String[] num = msg.getText().split(",", 2);
                int first = Integer.parseInt(num[0]);
                int second = Integer.parseInt(num[1]);
                
                //calculate prime number
                CountPrime cp = new CountPrime();
                int answer = cp.countNum(first, second);
                
                //send result to client
                TextMessage result = session.createTextMessage();
                result.setText("The number of primes between " + first + " and " + second + " is " + answer);
                result.setJMSCorrelationID("12345");
                result.setJMSReplyTo(tempDest);
                producer.send(result);
                System.out.println("Sending message: The number of primes between " + first + " and " + second + " is " + answer);
            } else {
                System.err.println("Message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
        
    }
}
