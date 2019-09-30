package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

}
