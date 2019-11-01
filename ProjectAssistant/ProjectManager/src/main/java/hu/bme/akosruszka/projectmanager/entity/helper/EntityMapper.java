package hu.bme.akosruszka.projectmanager.entity.helper;

import hu.bme.akosruszka.projectmanager.dto.*;
import hu.bme.akosruszka.projectmanager.entity.*;

import java.util.stream.Collectors;

public class EntityMapper {

    private EntityMapper() {
    }

    public static ProjectDTO entityToDTO(final Project project) {
        return ProjectDTO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .ownerName(project.getProjectOwnerEmail())
                .taskSet(project.getTaskNameSet())
                .participantSet(project.getParticipantEmailSet())
                .meetingSet(project.getMeetingNameSet())
                .build();
    }

    public static MeetingDTO entityToDTO(final Meeting meeting) {
        return MeetingDTO.builder()
                .name(meeting.getName())
                .description(meeting.getDescription())
                .projectName(meeting.getProjectName())
                .location(meeting.getLocation())
                .date(meeting.getDate())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .minuteName(meeting.getMinuteName())
                .chairPersonEmail(meeting.getChairPersonEmail())
                .attendeeEmailSet(meeting.getAttendeeEmailSet())
                .build();
    }

    public static UserDTO entityToDTO(final User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .projectNameSet(user.getProjectNameSet())
                .roleSet(user.getRoleSet())
                .meetingNameSet(user.getMeetingNameSet())
                .taskNameSet(user.getTaskNameSet())
                .build();
    }

    public static TaskDTO entityToDTO(final Task task) {
        return TaskDTO.builder()
                .info(task.getInfo())
                .projectName(task.getProjectName())
                .developerEmail(task.getDeveloperEmail())
                .build();
    }

    public static MinutesDTO entityToDTO(final Minutes minutes) {
        return MinutesDTO.builder()
                .title(minutes.getTitle())
                .meetingName(minutes.getMeetingName())
                .absentEmailSet(minutes.getAbsentEmailSet())
                .taskSet(minutes.getTaskSet().stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toSet()))
                .build();
    }

    public static ProjectDTO entityToDtoOtherCall(final Project project) {
        return ProjectDTO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .ownerName(project.getProjectOwnerEmail())
                .build();
    }

    public static MeetingDTO entityToDtoOtherCall(final Meeting meeting) {
        return MeetingDTO.builder()
                .name(meeting.getName())
                .date(meeting.getDate())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .location(meeting.getLocation())
                .build();
    }

    public static UserDTO entityToDtoOtherCall(final User user) {
        return user == null ? null : UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static TaskDTO entityToDtoOtherCall(final Task task) {
        return TaskDTO.builder()
                .info(entityToDtoOtherCall(task.getInfo()))
                .developerEmail(task.getDeveloperEmail())
                .projectName(task.getProjectName())
                .build();
    }

    private static TaskDescription entityToDtoOtherCall(final TaskDescription dto) {
        return TaskDescription.builder()
                .name(dto.getName())
                .estimatingDate(dto.getEstimatingDate())
                .description(dto.getDescription())
                .build();
    }

    public static MinutesDTO entityToDtoOtherCall(final Minutes minutes) {
        return MinutesDTO.builder()
                .title(minutes.getTitle())
                .absentEmailSet(minutes.getAbsentEmailSet())
                .taskSet(minutes.getTaskSet().stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toSet()))
                .build();
    }

    public static User userDtoToEntity(final UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .roleSet(dto.getRoleSet())
                .build();
    }
}
