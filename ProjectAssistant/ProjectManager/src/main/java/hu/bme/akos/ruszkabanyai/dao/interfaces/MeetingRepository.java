package hu.bme.akos.ruszkabanyai.dao.interfaces;

import hu.bme.akos.ruszkabanyai.entity.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends Saver<Meeting>, Remover<Meeting> {

    Optional<Meeting> findByName(String meetingName);

    List<Meeting> findAllByAttendeeList_Email(String userEmail);

}
