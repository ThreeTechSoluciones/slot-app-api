package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
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
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Student> students = Collections.emptyList();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Price> prices = new ArrayList<>(
            List.of(
                    new Price("1 dia"),
                    new Price("2 dias"),
                    new Price("3 dias"),
                    new Price("4 dias"),
                    new Price("Clase")
            )
    );

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
