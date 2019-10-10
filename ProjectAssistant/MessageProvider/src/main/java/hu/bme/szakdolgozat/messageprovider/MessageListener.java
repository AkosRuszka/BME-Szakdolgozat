package hu.bme.szakdolgozat.messageprovider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @JmsListener(destination = "${activemq.queue.name}")
    public void receiveMessage(String message) {
        log.error("Jött egy ilyen üzenet: " + message);
    }

}
