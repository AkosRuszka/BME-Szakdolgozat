package hu.bme.szakdolgozat.messageprovider.service;

import hu.bme.szakdolgozat.messageprovider.helper.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email.enabled}")
    private Boolean EMAIL_SENDING_ENABLED;

    @Async
    public void sendSimpleMessage(MyMessage message) throws MessagingException {
        if (EMAIL_SENDING_ENABLED) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(message.getTo());
            helper.setSubject(message.getSubject());
            helper.setText(message.getContent(), true);

            javaMailSender.send(mimeMessage);
            log.info("Elküldtünk egy üzenetet neki: " + message.getTo());
        } else {
            log.info("Az üzenet nem lett elküldve.");
        }
    }

}
