package com.solarsan.sabrage.security;

import static com.solarsan.sabrage.security.Permission.PLAYER_ACTIVATE;
import static com.solarsan.sabrage.security.Permission.PLAYER_GET_ALL;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_APPLY;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_CREATE;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_SET_RESULT;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_UPDATE;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(PLAYER_GET_ALL, PLAYER_ACTIVATE, TOURNAMENT_UPDATE, TOURNAMENT_SET_RESULT, TOURNAMENT_CREATE)),
    PLAYER(Set.of(TOURNAMENT_CREATE, TOURNAMENT_APPLY));

    private Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return permissions
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toSet());
    }
}
