package com.solarsan.sabrage.tournament;

import java.sql.Timestamp;

public record UpdateTournamentDTO(
        String description,
        Discipline discipline,
        Timestamp time,
        Timestamp applicationDeadline,
        int maxParticipants
) {
}
