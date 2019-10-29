package hu.bme.szakdolgozat.projectmanager.dao;

import hu.bme.szakdolgozat.projectmanager.entity.Meeting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findAllByNameIn(List<String> names, Pageable pageable);

    Optional<Meeting> findByName(String name);

}
