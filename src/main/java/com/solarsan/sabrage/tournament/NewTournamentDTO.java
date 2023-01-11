package com.solarsan.sabrage.tournament;

import java.sql.Timestamp;

public record NewTournamentDTO(
        String name,
        String organizer,
        String description,
        Discipline discipline,
        Timestamp time,
        Timestamp applicationDeadline,
        int maxParticipants
) {
}
