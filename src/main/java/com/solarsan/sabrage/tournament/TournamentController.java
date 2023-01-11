package com.solarsan.sabrage.tournament;

import com.solarsan.sabrage.tournament.game.GameResult;
import com.solarsan.sabrage.tournament.participant.NewParticipantDTO;
import com.solarsan.sabrage.tournament.participant.ParticipantDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class TournamentController {

    private final TournamentService service;

    @PostMapping(value = "/tournaments")
    public ResponseEntity<?> create(@RequestBody final NewTournamentDTO dto, final Authentication authentication) {
        if (!authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
                          .contains(dto.organizer())) {
            return ResponseEntity.badRequest().body("Cannot create tournament for another user.");
        }
        final TournamentDTO created = service.create(dto);
        final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().replacePath("/tournaments/{name}").buildAndExpand(created.name()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping(value = "/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/tournaments/{name}")
    public ResponseEntity<TournamentDTO> get(@PathVariable final String name) {
        return service
            .get(name)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/tournaments/{name}")
    public ResponseEntity<TournamentDTO> update(@PathVariable final String name,
                                                @RequestBody final UpdateTournamentDTO dto) {
        final TournamentDTO updated = service.update(name, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping(value = "/tournaments/{name}/participants")
    public ResponseEntity<?> apply(@PathVariable final String name,
                                   @RequestBody final NewParticipantDTO dto,
                                   final Authentication authentication) {
        if (!authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
                          .contains(dto.player())) {
            return ResponseEntity.badRequest().body("Cannot apply for another user.");
        }
        try {
            final ParticipantDTO participant = service.applyParticipant(name, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(participant);
        } catch (final IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/tournaments/{name}/games/{id}")
    public ResponseEntity<Void> setResult(@PathVariable final String name,
                                          @PathVariable final int id,
                                          @RequestBody final GameResult result) {
        service.setResult(name, id, result);
        return ResponseEntity.ok().build();
    }
}
