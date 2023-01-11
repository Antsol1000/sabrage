package com.solarsan.sabrage.player;

import com.solarsan.sabrage.security.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    private String email;

    private String firstName;

    private String lastName;

    private String pass;

    private boolean active;

    private Role role;


    public PlayerDTO dto() {
        return new PlayerDTO(firstName, lastName, email, active);
    }
}
