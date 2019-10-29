package hu.bme.szakdolgozat.projectmanager.dao;

import hu.bme.szakdolgozat.projectmanager.entity.MailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface MailMessageRepository extends MongoRepository<MailTemplate, String> {

    Optional<MailTemplate> getByName(String name);

}
