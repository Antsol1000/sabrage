package com.solarsan.sabrage.tournament.game;

import com.solarsan.sabrage.tournament.participant.ParticipantDTO;

public record GameDTO(int id, ParticipantDTO player1, ParticipantDTO player2, GameResult result) {
}
