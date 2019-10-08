package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.entity.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findAllByNameIn(List<String> names);

    Optional<Meeting> findByName(String name);

}
