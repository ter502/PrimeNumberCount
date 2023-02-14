/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primeclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Destination;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    private MessageProducer replyProducer;
    private Session session;
    private Destination replyDest;
    private String correlationId;
    
    public TextListener(Session session) {
              
        this.session = session;
        try {
            replyProducer = session.createProducer(null);
        } catch (JMSException ex) {
            Logger.getLogger(TextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                if (!msg.getText().equals("Hello client")){
                    System.out.println("The result from the server is : " + msg.getText());
                }
            } else {
                System.err.println("Message is not a TextMessage");
            }
            
            // set up the reply message 
            replyDest = message.getJMSReplyTo();
            correlationId = message.getJMSCorrelationID();
            
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
        
    }
    
    public String getCorrelationId(){
        return correlationId;
    }
    
    public Destination getReplyDest(){
        return replyDest;
    }

}
