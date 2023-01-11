package com.solarsan.sabrage;

import com.solarsan.sabrage.player.NewPlayerDTO;
import com.solarsan.sabrage.player.PlayerService;
import com.solarsan.sabrage.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final PlayerService playerService;

    @Override
    public void run(final String... args) throws Exception {
        final NewPlayerDTO admin = new NewPlayerDTO("Sabrage", "Admin", "admin", "1234");
        playerService.create(admin, Role.ADMIN);
    }
}
