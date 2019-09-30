package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> getAllByNameIn(List<String> names);

}
