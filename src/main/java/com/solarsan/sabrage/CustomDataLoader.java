package com.solarsan.sabrage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solarsan.sabrage.player.NewPlayerDTO;
import com.solarsan.sabrage.player.PlayerDTO;
import com.solarsan.sabrage.player.PlayerService;
import com.solarsan.sabrage.security.Role;
import com.solarsan.sabrage.tournament.NewTournamentDTO;
import com.solarsan.sabrage.tournament.TournamentDTO;
import com.solarsan.sabrage.tournament.TournamentService;
import com.solarsan.sabrage.tournament.UpdateTournamentDTO;
import com.solarsan.sabrage.tournament.participant.NewParticipantDTO;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.sql.Timestamp;

@Component
@AllArgsConstructor
@Profile("local")
public class CustomDataLoader implements CommandLineRunner {

    private final PlayerService playerService;
    private final TournamentService tournamentService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(final String... args) throws Exception {
        final PlayerDTO player1 = playerService.create(objectMapper.readValue(
            ResourceUtils.getFile("classpath:requests/player1.json"), NewPlayerDTO.class), Role.PLAYER);
        final PlayerDTO player2 = playerService.create(objectMapper.readValue(
            ResourceUtils.getFile("classpath:requests/player2.json"), NewPlayerDTO.class), Role.PLAYER);

        final TournamentDTO tournament1 = tournamentService.create(objectMapper.readValue(
            ResourceUtils.getFile("classpath:requests/tournament1.json"), NewTournamentDTO.class));
        tournamentService.create(objectMapper.readValue(
            ResourceUtils.getFile("classpath:requests/tournament2.json"), NewTournamentDTO.class));
        tournamentService.create(objectMapper.readValue(
            ResourceUtils.getFile("classpath:requests/tournament3.json"), NewTournamentDTO.class));

        tournamentService.applyParticipant(
            tournament1.name(), new NewParticipantDTO(player1.email(), 1234, 1250));
        tournamentService.applyParticipant(
            tournament1.name(), new NewParticipantDTO(player2.email(), 5678, 1200));

        tournamentService.update(tournament1.name(), new UpdateTournamentDTO(
            tournament1.description(), tournament1.discipline(), tournament1.time(),
            new Timestamp(System.currentTimeMillis()), tournament1.maxParticipants()
        ));
    }
}
