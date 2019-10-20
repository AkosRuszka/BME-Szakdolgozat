package hu.bme.akosruszka.projectmanager.dao;

import hu.bme.akosruszka.projectmanager.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
}
