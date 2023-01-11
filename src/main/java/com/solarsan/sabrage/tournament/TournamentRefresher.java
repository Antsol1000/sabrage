package com.solarsan.sabrage.tournament;

import com.solarsan.sabrage.tournament.game.GameDTO;
import com.solarsan.sabrage.tournament.game.GameResult;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@AllArgsConstructor
public class TournamentRefresher {

    private final TournamentService service;

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    void refreshAllTournaments() {
        service.getAll().forEach(this::refresh);
    }

    private void refresh(final TournamentDTO dto) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.after(dto.applicationDeadline())
                && dto.games().isEmpty()) {
            service.generateGames(dto.name());
        }
        if (now.after(dto.time())
                && dto.games().stream().map(GameDTO::result).noneMatch(GameResult.NOT_FINISHED::equals)
                && !dto.games().isEmpty()) {
            final UUID winner = dto
                    .games().stream()
                    .map(x -> x.result() == GameResult.PLAYER_1 ? x.player1().id() : x.player2().id())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream()
                    .max(Comparator.comparingLong(Map.Entry::getValue))
                    .map(Map.Entry::getKey).orElseThrow();
            service.setWinner(dto.name(), winner);
        }
    }

}
