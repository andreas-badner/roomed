package gpse.team52.repository;

import java.util.Optional;
import java.util.UUID;

import gpse.team52.domain.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Role repository.
 */
public interface RoleRepository extends CrudRepository<Role, UUID> {
    /**
     * Find role by name.
     *
     * @param name The role name.
     * @return optional role.
     */
    Optional<Role> findByName(String name);
}
