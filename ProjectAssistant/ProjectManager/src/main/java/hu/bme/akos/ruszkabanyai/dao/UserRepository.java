package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    List<User> findAllByNameIn(List<String> names);

}
