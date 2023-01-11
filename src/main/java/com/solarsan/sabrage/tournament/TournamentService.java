package com.solarsan.sabrage.tournament;

import com.solarsan.sabrage.player.PlayerDTO;
import com.solarsan.sabrage.player.PlayerEntity;
import com.solarsan.sabrage.player.PlayerRepository;
import com.solarsan.sabrage.tournament.game.GameDTO;
import com.solarsan.sabrage.tournament.game.GameEntity;
import com.solarsan.sabrage.tournament.game.GameId;
import com.solarsan.sabrage.tournament.game.GameRepository;
import com.solarsan.sabrage.tournament.game.GameResult;
import com.solarsan.sabrage.tournament.game.NewGameDTO;
import com.solarsan.sabrage.tournament.participant.NewParticipantDTO;
import com.solarsan.sabrage.tournament.participant.ParticipantDTO;
import com.solarsan.sabrage.tournament.participant.ParticipantEntity;
import com.solarsan.sabrage.tournament.participant.ParticipantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class TournamentService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;

    public TournamentDTO dto(final TournamentEntity e) {
        final PlayerDTO organizer = e.getOrganizer().dto();
        final ParticipantDTO winner = e.getWinner() == null ? null : e.getWinner().dto();
        final List<ParticipantDTO> participants =
            participantRepository.getAllByTournament(e).stream().map(ParticipantEntity::dto).toList();
        final List<GameDTO> games =
            gameRepository.getAllByTournament(e.getName()).stream().map(GameEntity::dto).toList();

        return new TournamentDTO(
            e.getName(), e.getDescription(), e.getDiscipline(), e.getTime(), e.getApplicationDeadline(),
            e.getMaxParticipants(), organizer, winner, participants, games);
    }

    public TournamentDTO create(final NewTournamentDTO dto) {
        final TournamentEntity saved = tournamentRepository.save(new TournamentEntity(
            dto.name(), playerRepository.findById(dto.organizer()).orElseThrow(),
            dto.description(), dto.discipline(), dto.time(), dto.applicationDeadline(),
            dto.maxParticipants(), null));
        log.info("Created new tournament {} by {}.", saved.getName(), saved.getOrganizer());
        return dto(saved);
    }

    public List<TournamentDTO> getAll() {
        return tournamentRepository
            .findAll()
            .stream()
            .map(this::dto)
            .sorted(Comparator.comparing(TournamentDTO::time))
            .toList();
    }

    public Optional<TournamentDTO> get(final String name) {
        return tournamentRepository.findById(name).map(this::dto);
    }

    public TournamentDTO update(final String name, final UpdateTournamentDTO dto) {
        final TournamentEntity e = tournamentRepository.findById(name).orElseThrow();
        e.setDescription(dto.description());
        e.setTime(dto.time());
        e.setApplicationDeadline(dto.applicationDeadline());
        e.setMaxParticipants(dto.maxParticipants());
        final TournamentEntity saved = tournamentRepository.save(e);
        log.info("Tournament {} updated.", e.getName());
        return dto(saved);
    }

    public ParticipantDTO applyParticipant(final String tournament, final NewParticipantDTO dto) {
        final TournamentEntity tournamentEntity = tournamentRepository.findById(tournament).orElseThrow();
        final PlayerEntity playerEntity = playerRepository.findById(dto.player()).orElseThrow();

        if (participantRepository.findByTournamentAndPlayer(tournamentEntity, playerEntity).isPresent()) {
            throw new IllegalStateException("You have already applied to this tournament!");
        }

        if (new Timestamp(System.currentTimeMillis()).after(tournamentEntity.getApplicationDeadline())) {
            throw new IllegalStateException("Application closed!");
        }

        final int currentParticipants = participantRepository.getAllByTournament(tournamentEntity).size();
        if (tournamentEntity.getMaxParticipants() == currentParticipants) {
            throw new IllegalStateException("Maximum number of participants reached.");
        }

        final ParticipantEntity saved = participantRepository.save(new ParticipantEntity(
            UUID.randomUUID(), playerEntity, dto.license(), dto.ranking(), tournamentEntity));
        log.info("Registered new participant {} for {}", saved.getPlayer(), saved.getTournament());
        return saved.dto();
    }

    public void setResult(final String tournament, final int id, final GameResult result) {
        tournamentRepository.findById(tournament).orElseThrow();
        final GameEntity e = gameRepository.findById(new GameId(id, tournament)).orElseThrow();
        e.setResult(result.get());
        gameRepository.save(e);
        log.info("Game {} in tournament {} finished with result {}", e.getId(), e.getTournament(), result);
    }

    public void setWinner(final String tournament, final UUID winner) {
        final TournamentEntity e = tournamentRepository.findById(tournament).orElseThrow();
        final ParticipantEntity winnerEntity = participantRepository.findById(winner).orElseThrow();
        e.setWinner(winnerEntity);
        tournamentRepository.save(e);
        log.info("Winner {} for tournament saved.", e.getName());
    }

    @Transactional
    public void generateGames(final String tournament) {
        final TournamentEntity e = tournamentRepository.findById(tournament).orElseThrow();
        final List<ParticipantEntity> participants = participantRepository.getAllByTournament(e);
        final AtomicInteger counter = new AtomicInteger();
        participants.forEach(p1 -> participants.forEach(p2 -> {
            if (!p1.equals(p2)) {
                createGame(e, new NewGameDTO(counter.get(), p1.getId(), p2.getId()));
                log.info("Created game {} for {}", counter.getAndIncrement(), e.getName());
            }
        }));
    }

    private GameDTO createGame(final TournamentEntity e, final NewGameDTO dto) {
        final GameEntity saved = gameRepository.save(
            new GameEntity(dto.id(), e.getName(),
                           participantRepository.findById(dto.player1()).orElseThrow(),
                           participantRepository.findById(dto.player2()).orElseThrow(),
                           null));
        log.info("Registered new game for {}", saved.getTournament());
        return saved.dto();
    }

}
