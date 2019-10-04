package hu.bme.akos.ruszkabanyai.dao;

import hu.bme.akos.ruszkabanyai.dao.interfaces.RoleRepository;
import hu.bme.akos.ruszkabanyai.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends BaseRepository implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Role findByName(String name) {
        return null;
    }

    @Override
    public Role save(Role o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public void delete(Role o) {
        entityManager.remove(o);
    }
}
