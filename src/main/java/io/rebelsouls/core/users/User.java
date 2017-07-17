package io.rebelsouls.core.users;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = { @Index(columnList = "username", unique = true) })
public class User implements UserDetails {

    private static final long serialVersionUID = 545057790101918959L;

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "userid")
    @SequenceGenerator(name = "userid", sequenceName = "userid")
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String displayName;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;

    private boolean expired = false;
    private boolean locked = false;
    private boolean credentialsExpired = false;
    private boolean enabled = true;
    private boolean approved = false;
    private String validationToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public boolean hasRole(String role) {
        return getRoles().contains(Role.valueOf(role));
    }

}
