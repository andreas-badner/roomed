package gpse.team52.domain;

import gpse.team52.form.UserRegistrationForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User entity.
 */
@Entity
@NoArgsConstructor

public class User implements UserDetails { //NOPMD

    private static final long serialVersionUID = 7179581269044235932L;

    @Id
    @Getter
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Getter
    @Setter
    @Column(nullable = false)
    private String firstname;

    @Getter
    @Setter
    @Column(nullable = false)
    private String lastname;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="location_id",referencedColumnName="id")
    private Location location;

    @Getter
    @Setter
    @Column
    private String picture;

    @Column(unique = true, nullable = false)
    @Getter
    private String email;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean isEnabled = false;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Set<Role> roles = new HashSet<>();

    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    private Set<Privilege> privileges = new HashSet<>();

    /**
     * Create a user from a registration form.
     *
     * @param form     The form from which the user details should be retrieved.
     * @param password The encoded password for the user.
     */
    public User(final UserRegistrationForm form, final String password) {
        username = form.getUsername();
        firstname = form.getFirstName();
        lastname = form.getLastName();
        email = form.getEmail();
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFullName() {
        return firstname + ' ' + lastname;
    }

    /**
     * Assign a role to a user.
     *
     * @param role The rule to be added.
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
    }
}
