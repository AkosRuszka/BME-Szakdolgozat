package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.dao.interfaces.UserRepository;
import hu.bme.akos.ruszkabanyai.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return Optional.empty();
    }

    @Override
    public List<User> findAllByNameIn(List<String> names) {
        return null;
    }

    @Override
    public User save(User o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public void delete(User o) {
        entityManager.remove(o);
    }
}
