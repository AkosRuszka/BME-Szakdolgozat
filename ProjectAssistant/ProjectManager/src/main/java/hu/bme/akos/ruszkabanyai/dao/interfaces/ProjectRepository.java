package hu.bme.akos.ruszkabanyai.dao.interfaces;

import hu.bme.akos.ruszkabanyai.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends Saver<Project>, Remover<Project> {

    Optional<Project> findByName(String name);

}
