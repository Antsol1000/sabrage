package com.solarsan.sabrage.tournament.participant;

import com.solarsan.sabrage.player.PlayerEntity;
import com.solarsan.sabrage.tournament.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, UUID> {
    List<ParticipantEntity> getAllByTournament(final TournamentEntity tournament);
    Optional<ParticipantEntity> findByTournamentAndPlayer(final TournamentEntity tournament, final PlayerEntity player);
}
