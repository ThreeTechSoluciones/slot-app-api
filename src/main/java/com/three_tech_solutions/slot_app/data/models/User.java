package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Student> students = Collections.emptyList();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Plan> plans = Collections.emptyList();
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Slot> slots = Collections.emptyList();
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<SpecificSlot> specificSlots = Collections.emptyList();
    @OneToOne(cascade = CascadeType.ALL)
    private UserPreferences userPreferences = new UserPreferences();

    @Id
    private UUID id = UUID.randomUUID();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
