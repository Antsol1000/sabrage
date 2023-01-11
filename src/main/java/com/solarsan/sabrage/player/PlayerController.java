package com.solarsan.sabrage.player;

import com.solarsan.sabrage.security.LoginRequest;
import com.solarsan.sabrage.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
public class PlayerController {

    private final PlayerService service;

    @PostMapping(value = "/players")
    public ResponseEntity<PlayerDTO> create(@RequestBody final NewPlayerDTO dto) {
        final PlayerDTO created = service.create(dto, Role.PLAYER);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(value = "/players")
    public ResponseEntity<List<PlayerDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping(value = "/players/{email}")
    public ResponseEntity<Void> activate(@PathVariable final String email) {
        service.activate(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest) {
        try {
            service.validateEmailAndPassword(loginRequest.email(), loginRequest.password());
            return ResponseEntity.ok().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password");
        }
    }
}
