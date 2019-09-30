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
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .roleList(Collections.singleton(userRole))
                .build();
        userRepository.save(user);

        User user1 = User.builder()
                .name("test2")
                .email("test2@gmail.com")
                .roleList(Collections.singleton(userRole))
                .build();
        userRepository.save(user1);

        Project project = Project.builder()
                .name("sanyiProject")
                .description("Egy leírás a projekthez magyarul.")
                .projectOwner(user)
                .participantList(Collections.singletonList(user1))
                .build();
        projectRepository.save(project);

        Meeting meeting = Meeting.builder()
                .name("meeting1").description("meeting 1 leírása").project(project).location("haller")
                .date(Date.valueOf(LocalDate.now())).minute(null).chairPerson(user)
                .attendeeList(Collections.singletonList(user1)).build();
        meetingRepository.save(meeting);

        Task task = Task.builder()
                .info(TaskDescription.builder()
                        .name("Első feladat")
                        .description("Cél hogy nűködjön a perzisztencia")
                        .estimatingDate(Date.valueOf(LocalDate.now()))
                        .build())
                .project(project)
                .developer(user1)
                .build();
        taskRepository.save(task);
    }
}
