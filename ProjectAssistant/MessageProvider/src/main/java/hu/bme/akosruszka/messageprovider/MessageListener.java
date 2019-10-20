package hu.bme.akosruszka.messageprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.akosruszka.messageprovider.service.EmailService;
import hu.bme.akosruszka.messageprovider.helper.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    private EmailService emailService;

    @JmsListener(destination = "${activemq.queue.name}")
    public void receiveMessage(String object) {
        ObjectMapper mapper = new ObjectMapper();
        MyMessage message;
        try {
            message = mapper.readValue(object, MyMessage.class);
            log.error("Jött egy ilyen üzenet:\nto: " + message.getTo() + "subject: " + message.getSubject());
            emailService.sendSimpleMessage(message);
        } catch (IOException | MessagingException e) {
            log.error("Mapper exception", e);
        }
    }

}
