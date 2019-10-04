package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.dao.interfaces.ProjectRepository;
import hu.bme.akos.ruszkabanyai.entity.Project;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProjectRepositoryImpl extends BaseRepository implements ProjectRepository {
    public ProjectRepositoryImpl() {
        super(Project.class);
    }

    @Override
    public Optional<Project> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Project save(Project o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public void delete(Project o) {
        entityManager.remove(o);
    }
}
