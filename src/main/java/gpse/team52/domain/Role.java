package gpse.team52.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Role Entity.
 */
@Entity
@NoArgsConstructor
public class Role {

    /**
     * Role primary key.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    /**
     * Role name.
     */
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * List of privileges for a role.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    private Set<Privilege> privileges = new HashSet<>();

    /**
     * Role constructor.
     *
     * @param name The role name.
     */
    public Role(final String name) {
        this.name = name;
    }

    /**
     * Secondary role constructor.
     *
     * @param name       The role name.
     * @param privileges The role privileges.
     */
    public Role(final String name, final Set<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }
}
