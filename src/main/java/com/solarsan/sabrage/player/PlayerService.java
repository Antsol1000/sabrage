package com.solarsan.sabrage.player;

import com.solarsan.sabrage.security.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PlayerDTO create(final NewPlayerDTO dto, final Role role) {
        final PlayerEntity saved = repository.save(new PlayerEntity(
            dto.email(), dto.firstName(), dto.lastName(), passwordEncoder.encode(dto.pass()), true, role));
        log.info("Created new player {}", saved.getEmail());
        return saved.dto();
    }

    public List<PlayerDTO> getAll() {
        return repository
            .findAll()
            .stream()
            .map(PlayerEntity::dto)
            .toList();
    }

    public Optional<PlayerEntity> get(final String email) {
        return repository.findById(email);
    }

    public void activate(final String email) {
        final PlayerEntity e = repository.findById(email).orElseThrow();
        e.setActive(true);
        repository.save(e);
        log.info("Account for player {} was activated", e.getEmail());
    }

    public void validateEmailAndPassword(final String email, final String pass) {
        final PlayerEntity e = repository.findById(email).orElseThrow();
        if (!passwordEncoder.matches(pass, e.getPass())) {
            throw new IllegalStateException();
        }
    }
}
