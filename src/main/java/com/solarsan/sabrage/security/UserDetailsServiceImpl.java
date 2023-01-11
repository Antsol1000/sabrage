package com.solarsan.sabrage.security;

import com.solarsan.sabrage.player.PlayerEntity;
import com.solarsan.sabrage.player.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlayerService playerService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return playerService
            .get(username)
            .map(this::createUserDetails)
            .orElseThrow();
    }

    private CustomUserDetails createUserDetails(final PlayerEntity player) {
        final Set<SimpleGrantedAuthority> authorities = player.getRole().getGrantedAuthorities();
        authorities.add(new SimpleGrantedAuthority(player.getEmail()));

        final CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(player.getEmail());
        userDetails.setPassword(player.getPass());
        userDetails.setAuthorities(authorities);
        return userDetails;
    }
}
