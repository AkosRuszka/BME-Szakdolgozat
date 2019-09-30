package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByInfoName(String names);

}
