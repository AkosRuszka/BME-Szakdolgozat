package hu.bme.szakdolgozat.projectmanager.dao;

import hu.bme.szakdolgozat.projectmanager.entity.Minutes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface MinuteRepository extends MongoRepository<Minutes, String> {

    Optional<Minutes> findAllByTitle(String name);

}
