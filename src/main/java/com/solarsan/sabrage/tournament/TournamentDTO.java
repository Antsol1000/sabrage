package com.solarsan.sabrage.tournament;

import com.solarsan.sabrage.player.PlayerDTO;
import com.solarsan.sabrage.tournament.game.GameDTO;
import com.solarsan.sabrage.tournament.participant.ParticipantDTO;

import java.sql.Timestamp;
import java.util.List;

public record TournamentDTO(
        String name,
        String description,
        Discipline discipline,
        Timestamp time,
        Timestamp applicationDeadline,
        int maxParticipants,
        PlayerDTO organizer,
        ParticipantDTO winner,
        List<ParticipantDTO> participants,
        List<GameDTO> games
) {
}
