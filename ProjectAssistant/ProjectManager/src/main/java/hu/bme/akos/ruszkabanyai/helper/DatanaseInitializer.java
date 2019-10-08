package hu.bme.akos.ruszkabanyai.helper;

import hu.bme.akos.ruszkabanyai.dao.*;
import hu.bme.akos.ruszkabanyai.entity.*;
import hu.bme.akos.ruszkabanyai.entity.helper.TaskDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DatanaseInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private TaskRepository taskRepository;

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
                    .email("test2@gmail.com")
                    .roleSet(new HashSet<>(Collections.singleton(userRole)))
                    .build();

            Project project = Project.builder()
                    .name("sanyiProject")
                    .description("Egy leírás a projekthez magyarul.").build();
            project.setProjectOwner(user);
            project.setParticipantSet(new HashSet<>(Collections.singleton(user1)));

            Meeting meeting = Meeting.builder()
                    .name("meeting1").description("meeting 1 leírása").location("haller")
                    .date(Date.valueOf(LocalDate.now())).minuteName(null).build();
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

            roleRepository.save(userRole);
            userRepository.save(user);
            projectRepository.save(project);
            meetingRepository.save(meeting);
            userRepository.save(user1);
            taskRepository.save(task);
        }
    }
}
