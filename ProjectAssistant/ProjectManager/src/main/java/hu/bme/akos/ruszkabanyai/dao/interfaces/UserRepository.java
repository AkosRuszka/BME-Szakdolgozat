package hu.bme.akos.ruszkabanyai.dao.interfaces;

    import hu.bme.akos.ruszkabanyai.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Saver<User>, Remover<User> {

    Optional<User> findByEmail(String email);

    List<User> findAllByNameIn(List<String> names);

}
