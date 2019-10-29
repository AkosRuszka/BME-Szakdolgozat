package hu.bme.szakdolgozat.projectmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hu.bme.szakdolgozat.projectmanager.dao.MailMessageRepository;
import hu.bme.szakdolgozat.projectmanager.dao.UserRepository;
import hu.bme.szakdolgozat.projectmanager.entity.MailTemplate;
import hu.bme.szakdolgozat.projectmanager.entity.Meeting;
import hu.bme.szakdolgozat.projectmanager.entity.User;
import hu.bme.szakdolgozat.projectmanager.entity.helper.Message;
import hu.bme.szakdolgozat.projectmanager.helper.FoundEntityException;
import hu.bme.szakdolgozat.projectmanager.helper.NotFoundEntityException;
import hu.bme.szakdolgozat.projectmanager.helper.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.jms.Destination;
import java.io.IOException;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MessageService {

    private final JmsTemplate jmsTemplate;

    private final Destination destination;

    private final UserRepository userRepository;

    private final MailMessageRepository mailMessageRepository;

    public MessageService(JmsTemplate jmsTemplate, Destination destination, UserRepository userRepository, MailMessageRepository mailMessageRepository) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.userRepository = userRepository;
        this.mailMessageRepository = mailMessageRepository;
    }

    private void publishe(List<String> userEmails, String templateName, Map<String, Object> model) throws IOException, TemplateException, NotFoundEntityException {
        MailTemplate message = mailMessageRepository.getByName(templateName).orElseThrow(() -> new NotFoundEntityException(StringConstants.TEMPLATE_NOT_FOUND));
        ObjectMapper mapper = new ObjectMapper();

        String templateString = message.getMessage();
        String subject = message.getSubject();

        for (String email : userEmails) {
            User user = userRepository.findByEmail(email).get();
            model.put("subject", subject);
            model.put("userName", user.getName());
            model.put("dateTime", ((Meeting) model.get("meeting")).getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
            Template template = new Template("name", new StringReader(templateString), new Configuration(Configuration.VERSION_2_3_29));

            String readyMessage = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            jmsTemplate.convertAndSend(destination, mapper.writeValueAsString(Message.builder().to(email).subject(subject).content(readyMessage).build()));
        }
    }

    public Optional<ResponseEntity> publish(List<String> userEmails, String templateName, Map<String, Object> model) {
        try {
            publishe(userEmails, templateName, model);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage());
            return Optional.of(ResponseEntity.status(HttpStatus.CONFLICT).body(StringConstants.TEMPLATE_EXCEPTION));
        } catch (NotFoundEntityException | FoundEntityException e) {
            log.error(e.getMessage());
            return Optional.of(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
        }
        return Optional.empty();
    }
}
