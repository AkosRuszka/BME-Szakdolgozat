package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.dao.interfaces.TaskRepository;
import hu.bme.akos.ruszkabanyai.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TaskRepositoryImpl extends BaseRepository implements TaskRepository {
    public TaskRepositoryImpl() {
        super(Task.class);
    }

    @Override
    public Optional<Task> findByInfoName(String names) {
        return Optional.empty();
    }

    @Override
    public Task save(Task o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public void delete(Task o) {
        entityManager.remove(o);
    }
}
