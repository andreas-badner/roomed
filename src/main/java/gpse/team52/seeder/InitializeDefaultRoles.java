package gpse.team52.seeder;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.annotation.PostConstruct;

import gpse.team52.contract.PrivilegeService;
import gpse.team52.contract.RoleService;
import gpse.team52.domain.Privilege;
import gpse.team52.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Initialize default positions in the database.
 */

@Service
public class InitializeDefaultRoles {
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    private final InitializeDefaultPrivilege initializeDefaultRoles;

    /**
     * Constructor for the used services.
     *
     * @param roleService            Service for positions
     * @param privilegeService       Service for positios
     * @param initializeDefaultRoles Initialize default roles.
     */
    @Autowired
    public InitializeDefaultRoles(final RoleService roleService,
                                  final PrivilegeService privilegeService,
                                  final InitializeDefaultPrivilege initializeDefaultRoles) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.initializeDefaultRoles = initializeDefaultRoles;
    }

    /**
     * Initializes privileges to test.
     */
    @PostConstruct
    public void init() {
        final Iterable<Privilege> privileges = this.privilegeService.getAll();
        final Hashtable<String, Privilege> hashtable = new Hashtable<>();
        for (final Privilege privilege : privileges) {
            hashtable.put(privilege.getName(), privilege);
        }
        final Set<Privilege> allPrivileges = new HashSet<>(hashtable.values());

        roleService.create(new Role("ROLE_ADMIN", allPrivileges));
        roleService.create(new Role("ROLE_STUDENT", Set.of(hashtable.get("create_meeting"))));
        roleService.create(new Role("ROLE_SECRETARY", Set.of(
        hashtable.get("create_meeting"),
        hashtable.get("delete_meeting"),
        hashtable.get("disable_rebook_meeting")
        )));
        roleService.create(new Role("ROLE_USER", Set.of()));
    }
}
