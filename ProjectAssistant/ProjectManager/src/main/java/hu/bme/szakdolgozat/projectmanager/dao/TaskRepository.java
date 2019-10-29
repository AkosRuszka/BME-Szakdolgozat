package hu.bme.szakdolgozat.projectmanager.dao;

import hu.bme.szakdolgozat.projectmanager.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TaskRepository extends MongoRepository<Task, String> {

    Optional<Task> findByTaskName(String name);

    List<Task> findAllByInfoNameIn(List<String> names, Pageable pageable);

}
