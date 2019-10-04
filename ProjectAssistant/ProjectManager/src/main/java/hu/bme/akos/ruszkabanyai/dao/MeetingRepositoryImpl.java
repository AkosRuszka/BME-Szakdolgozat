package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.dao.interfaces.MeetingRepository;
import hu.bme.akos.ruszkabanyai.entity.Meeting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MeetingRepositoryImpl extends BaseRepository implements MeetingRepository {
    public MeetingRepositoryImpl() {
        super(Meeting.class);
    }

    @Override
    public Optional<Meeting> findByName(String meetingName) {
        return Optional.empty();
    }

    @Override
    public List<Meeting> findAllByAttendeeList_Email(String userEmail) {
        return null;
    }

    @Override
    public Meeting save(Meeting o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public void delete(Meeting o) {
        entityManager.remove(o);
    }
}
