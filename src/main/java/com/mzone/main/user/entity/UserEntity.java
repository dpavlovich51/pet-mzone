package com.mzone.main.user.entity;

import com.mzone.main.core.Gender;
import com.mzone.main.entity.UserRole;
import com.mzone.main.core.BaseEntity;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "birthday")
    private DateTime birthday;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.ORDINAL)
    private Set<UserRole> roles;

    public UserEntity() {
    }

    private UserEntity(UUID id,
                      String firstName,
                      String lastName,
                      Gender gender,
                      DateTime birthday,
                      EmailValue email,
                      String password,
                      Set<UserRole> roles) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email.getValue();
        this.password = password;
        this.roles = roles;
    }

    public static UserEntity create(UUID id,
                                    String firstName,
                                    String lastName,
                                    Gender gender,
                                    DateTime birthday,
                                    EmailValue email,
                                    String password,
                                    Set<UserRole> roles) {
        return new UserEntity(
                id,
                firstName,
                lastName,
                gender,
                birthday,
                email,
                password,
                roles
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    private Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    @Override
    public String getUsername() {
        return getEmail();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}

