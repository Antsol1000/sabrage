package com.solarsan.sabrage.tournament.participant;

import com.solarsan.sabrage.player.PlayerDTO;

import java.util.UUID;

public record ParticipantDTO(UUID id, PlayerDTO player, int license, int ranking) {
}
