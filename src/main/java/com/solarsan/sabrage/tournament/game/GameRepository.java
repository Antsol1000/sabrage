package com.solarsan.sabrage.tournament.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, GameId> {
    List<GameEntity> getAllByTournament(final String tournament);
}
