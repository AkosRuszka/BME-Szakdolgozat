package hu.bme.akosruszka.projectmanager.dao;

import hu.bme.akosruszka.projectmanager.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findAll();

    Optional<Project> findByName(String name);

    List<Project> findAllByNameIn(List<String> names);

}
