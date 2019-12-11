package hu.bme.akosruszka.projectmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hu.bme.akosruszka.projectmanager.constans.StringConstants;
import hu.bme.akosruszka.projectmanager.dao.MailMessageRepository;
import hu.bme.akosruszka.projectmanager.dao.UserRepository;
import hu.bme.akosruszka.projectmanager.entity.MailTemplate;
import hu.bme.akosruszka.projectmanager.entity.Meeting;
import hu.bme.akosruszka.projectmanager.entity.Project;
import hu.bme.akosruszka.projectmanager.entity.User;
import hu.bme.akosruszka.projectmanager.entity.helper.Message;
import hu.bme.akosruszka.projectmanager.helper.FoundEntityException;
import hu.bme.akosruszka.projectmanager.helper.NotFoundEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.HttpServerErrorException;

import javax.jms.Destination;
import java.io.IOException;
import java.io.StringReader;
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

            if (model.get("meeting") != null) {
                Meeting meeting = ((Meeting) model.get("meeting"));
                String date = String.format("%s %s - %s", meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
                model.put("dateTime", date);
            } else if (model.get("project") != null) {
                Project project = ((Project) model.get("project"));
                model.put("project", project);
            }


            Template template = new Template("name", new StringReader(templateString), new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));

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
        } catch (HttpServerErrorException.InternalServerError e) {
            log.error(e.getMessage());
            return Optional.of(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Messaging server not available"));
        }
        return Optional.empty();
    }
}
