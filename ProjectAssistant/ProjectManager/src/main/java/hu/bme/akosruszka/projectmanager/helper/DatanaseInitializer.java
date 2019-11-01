package hu.bme.akosruszka.projectmanager.helper;

import hu.bme.akosruszka.projectmanager.constans.StringConstants;
import hu.bme.akosruszka.projectmanager.dao.*;
import hu.bme.akosruszka.projectmanager.entity.*;
import hu.bme.akosruszka.projectmanager.entity.helper.TaskDescription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

            User akos = User.builder()
                    .name("test2")
                    .email("akosruszka@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            User user2 = User.builder()
                    .name("test3")
                    .email("test3@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            User user3 = User.builder()
                    .name("test4")
                    .email("test4@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            User user4 = User.builder()
                    .name("test5")
                    .email("test5@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            Project project = Project.builder()
                    .name("sanyiProject")
                    .description("Egy leírás a projekthez magyarul.").build();
            project.setProjectOwner(user);
            project.setParticipantSet(Set.of(akos, user2, user3));

            Project project2 = Project.builder()
                    .name("ChangedProject")
                    .description("A második teszt projekt egy picit hosszabb leírással. Megjelenítés illetve működés teszteléséhez.")
                    .build();
            project2.setProjectOwner(akos);
            project2.setParticipantSet(Set.of(user4, user3));

            Meeting meeting = Meeting.builder()
                    .name("Első meeting").description("Az első meeting leírása egy rövidebb szösszenet hogy meg" +
                            "lehessen tekinteni egy akár hosszabb meeting leírásának html oldalban lévő megjelenítését.")
                    .location("Haller Garden D épület 1. emelet 2H tárgyaló")
                    .date(LocalDate.now())
                    .startTime(LocalTime.now())
                    .endTime(LocalTime.now().plusHours(2))
                    .minuteName(null).build();
            meeting.setChairPerson(user);
            meeting.setProject(project);
            meeting.setAttendeeSet(Set.of(akos, user2, user3));

            Meeting meeting2 = Meeting.builder()
                    .name("Második meeting").description("Az első meeting leírása egy rövidebb szösszenet hogy meg" +
                            "lehessen tekinteni egy akár hosszabb meeting leírásának html oldalban lévő megjelenítését.")
                    .location("Haller Garden E épület 6. emelet 2B tárgyaló")
                    .date(LocalDate.now())
                    .startTime(LocalTime.now().minusHours(1))
                    .endTime(LocalTime.now().plusHours(1))
                    .minuteName(null).build();
            meeting2.setChairPerson(akos);
            meeting2.setProject(project2);
            meeting2.setAttendeeSet(Set.of(user4, user3));


            Task task = Task.builder()
                    .info(TaskDescription.builder()
                            .name("Első feladat")
                            .description("Cél hogy nűködjön a perzisztencia")
                            .estimatingDate(Date.valueOf(LocalDate.now()))
                            .build())
                    .build();
            task.setTaskName(task.getInfo().getName());
            task.setProject(project);
            task.setDeveloper(akos);

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
            projectRepository.save(project2);
            meetingRepository.save(meeting);
            meetingRepository.save(meeting2);
            userRepository.save(akos);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            taskRepository.save(task);
            mailMessageRepository.save(mailMessage);
            mailMessageRepository.save(mailMessage2);
            mailMessageRepository.save(mailMessage3);
            mailMessageRepository.save(mailMessage4);
        }
    }
}
