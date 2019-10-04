package hu.bme.akos.ruszkabanyai.dao.interfaces;

import hu.bme.akos.ruszkabanyai.entity.Role;

public interface RoleRepository extends Saver<Role>, Remover<Role> {
    Role findByName(String name);
}
