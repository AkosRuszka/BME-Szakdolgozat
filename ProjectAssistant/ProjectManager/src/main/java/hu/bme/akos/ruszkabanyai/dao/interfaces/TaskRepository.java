package hu.bme.akos.ruszkabanyai.dao.interfaces;

import hu.bme.akos.ruszkabanyai.entity.Task;

import java.util.Optional;

public interface TaskRepository extends Saver<Task>, Remover<Task> {

    Optional<Task> findByInfoName(String names);

}
