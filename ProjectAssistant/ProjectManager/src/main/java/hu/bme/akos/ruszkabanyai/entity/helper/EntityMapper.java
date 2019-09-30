package hu.bme.akos.ruszkabanyai.entity.helper;

import hu.bme.akos.ruszkabanyai.dto.*;
import hu.bme.akos.ruszkabanyai.entity.*;

import java.util.stream.Collectors;

public class EntityMapper {

    public static ProjectDTO entityToDTO(final Project project) {
        return ProjectDTO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .meetingList(project.getMeetingList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .participantList(project.getParticipantList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .owner(EntityMapper.entityToDtoOtherCall(project.getProjectOwner()))
                .taskList(project.getTaskList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .build();
    }

    public static MeetingDTO entityToDTO(final Meeting meeting) {
        return MeetingDTO.builder()
                .name(meeting.getName())
                .description(meeting.getDescription())
                .project(EntityMapper.entityToDtoOtherCall(meeting.getProject()))
                .location(meeting.getLocation())
                .date(meeting.getDate())
                .minute(EntityMapper.entityToDtoOtherCall(meeting.getMinute()))
                .chairPerson(EntityMapper.entityToDtoOtherCall(meeting.getChairPerson()))
                .attendeeList(meeting.getAttendeeList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .build();
    }

    public static UserDTO entityToDTO(final User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .ownProjectList(user.getOwnProjectList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .participanProjectList(user.getParticipanProjectList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .roleList(user.getRoleList())
                .ownMeetingList(user.getOwnMeetingList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .meetingList(user.getMeetingList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .taskList(user.getTaskList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .build();
    }

    public static TaskDTO entityToDTO(final Task task) {
        return TaskDTO.builder()
                .info(task.getInfo())
                .project(EntityMapper.entityToDtoOtherCall(task.getProject()))
                .developer(EntityMapper.entityToDtoOtherCall(task.getDeveloper()))
                .build();
    }

    public static MinutesDTO entityToDTO(final Minutes minutes) {
        return MinutesDTO.builder()
                .meeting(EntityMapper.entityToDtoOtherCall(minutes.getMeeting()))
                .absentList(minutes.getAbsentList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .taskList(minutes.getTaskList())
                .build();
    }

    public static ProjectDTO entityToDtoOtherCall(final Project project) {
        return ProjectDTO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .owner(entityToDtoOtherCall(project.getProjectOwner()))
                .build();
    }

    public static MeetingDTO entityToDtoOtherCall(final Meeting meeting) {
        return MeetingDTO.builder()
                .name(meeting.getName())
                .date(meeting.getDate())
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
                .developer(entityToDtoOtherCall(task.getDeveloper()))
                .project(entityToDtoOtherCall(task.getProject()))
                .build();
    }

    public static TaskDescription entityToDtoOtherCall(final TaskDescription dto) {
        return TaskDescription.builder()
                .name(dto.getName())
                .estimatingDate(dto.getEstimatingDate())
                .build();
    }

    public static MinutesDTO entityToDtoOtherCall(final Minutes minutes) {
        return MinutesDTO.builder()
                .absentList(minutes.getAbsentList()
                        .stream().map(EntityMapper::entityToDtoOtherCall).collect(Collectors.toList()))
                .taskList(minutes.getTaskList())
                .build();
    }

    private static User userDtoToEntity(final UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .roleList(dto.getRoleList())
                .build();
    }
}
