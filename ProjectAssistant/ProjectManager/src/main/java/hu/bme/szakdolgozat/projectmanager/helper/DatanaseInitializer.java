package hu.bme.szakdolgozat.projectmanager.helper;

import hu.bme.akos.ruszkabanyai.dao.*;
import hu.bme.akos.ruszkabanyai.entity.*;
import hu.bme.szakdolgozat.projectmanager.entity.*;
import hu.bme.szakdolgozat.projectmanager.entity.helper.TaskDescription;
import hu.bme.szakdolgozat.projectmanager.dao.MailMessageRepository;
import hu.bme.szakdolgozat.projectmanager.dao.RoleRepository;
import hu.bme.szakdolgozat.projectmanager.dao.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DatanaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final MeetingRepository meetingRepository;

    private final TaskRepository taskRepository;

    private final MailMessageRepository mailMessageRepository;

    public DatanaseInitializer(RoleRepository roleRepository, UserRepository userRepository, ProjectRepository projectRepository, MeetingRepository meetingRepository, TaskRepository taskRepository, MailMessageRepository mailMessageRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.meetingRepository = meetingRepository;
        this.taskRepository = taskRepository;
        this.mailMessageRepository = mailMessageRepository;
    }

    @Override
    public void run(String... args) {
        if (false) {
            Role userRole = new Role("ROLE_USER");

            User user = User.builder()
                    .name("test")
                    .email("test@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            User user1 = User.builder()
                    .name("test2")
                    .email("akosruszka@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            Project project = Project.builder()
                    .name("sanyiProject")
                    .description("Egy leírás a projekthez magyarul.").build();
            project.setProjectOwner(user);
            project.setParticipantSet(new HashSet<>(Collections.singleton(user1)));

            Meeting meeting = Meeting.builder()
                    .name("meeting1").description("meeting 1 leírása").location("haller")
                    .date(LocalDateTime.now()).minuteName(null).build();
            meeting.setChairPerson(user);
            meeting.setProject(project);
            meeting.setAttendeeSet(new HashSet<>(Collections.singleton(user1)));

            Task task = Task.builder()
                    .info(TaskDescription.builder()
                            .name("Első feladat")
                            .description("Cél hogy nűködjön a perzisztencia")
                            .estimatingDate(Date.valueOf(LocalDate.now()))
                            .build())
                    .build();
            task.setTaskName(task.getInfo().getName());
            task.setProject(project);
            task.setDeveloper(user1);

            String meetingNewMessage = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>${subject}</title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                    "    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Roboto', sans-serif;\n" +
                    "            font-size: 48px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#eaeaea\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                    "                <p>Kedves ${userName},</p>\n" +
                    "                <p>Meghívást kaptál az alábbi Meetingre: ${meeting.name} , amely a ${meeting.projectName} -hez fűződik.</p>\n" +
                    "                <p>A Meeting időpontja: ${dateTime}</p>\n" +
                    "                <p>A Meeting helyszíne: ${meeting.location}</p>\n" +
                    "                <p>A Meetinget vezeti: ${meeting.chairPersonEmail}</p>\n" +
                    "                <p>Köszönjük a részvételt.</p>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            MailTemplate mailMessage = MailTemplate.builder()
                    .name(StringConstants.MEETING_NEW)
                    .message(meetingNewMessage)
                    .subject("Új meeting meghívás!").build();

            String meetingUpdateMessage = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>${subject}</title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                    "    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Roboto', sans-serif;\n" +
                    "            font-size: 48px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#eaeaea\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                    "                <p>Kedves ${userName},</p>\n" +
                    "                <p>Frissült az alábbi Meeting: ${meeting.name} , amely a ${meeting.projectName} -hez fűződik.</p>\n" +
                    "                <p>A Meeting időpontja: ${dateTime}</p>\n" +
                    "                <p>A Meeting helyszíne: ${meeting.location}</p>\n" +
                    "                <p>A Meetinget vezeti: ${meeting.chairPersonEmail}</p>\n" +
                    "                <p>További információt a honlapon találsz.\n" +
                    "                <p>Köszönjük a részvételt.</p>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            MailTemplate mailMessage2 = MailTemplate.builder()
                    .name(StringConstants.MEETING_UPDATED)
                    .message(meetingUpdateMessage)
                    .subject("Frissült meeting!").build();

            String meetingDelete = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>${subject}</title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                    "    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Roboto', sans-serif;\n" +
                    "            font-size: 48px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#eaeaea\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                    "                <p>Kedves ${userName},</p>\n" +
                    "                <p>Az alábbi meeting: ${meeting.name} törlésre került, amely a ${meeting.projectName} -hez fűződött.</p>\n" +
                    "                <p>Köszönjük.</p>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            MailTemplate mailMessage3 = MailTemplate.builder()
                    .name(StringConstants.MEETING_DELETED)
                    .message(meetingDelete)
                    .subject("Törölt meeting!").build();

            String meetingUserRemove = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>${subject}</title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                    "    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Roboto', sans-serif;\n" +
                    "            font-size: 48px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#eaeaea\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                    "                <p>Kedves ${userName},</p>\n" +
                    "                <p>Az alábbi meeting: ${meeting.name} -ről amely a ${meeting.projectName} -hez fűződött, levételre kerültél.</p>\n" +
                    "                <p>Köszönjük.</p>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            MailTemplate mailMessage4 = MailTemplate.builder()
                    .name(StringConstants.MEETING_REMOVED_USER)
                    .message(meetingUserRemove)
                    .subject("Levettek a meetingről").build();

            roleRepository.save(userRole);
            userRepository.save(user);
            projectRepository.save(project);
            meetingRepository.save(meeting);
            userRepository.save(user1);
            taskRepository.save(task);
            mailMessageRepository.save(mailMessage);
            mailMessageRepository.save(mailMessage2);
            mailMessageRepository.save(mailMessage3);
            mailMessageRepository.save(mailMessage4);
        }
    }
}
