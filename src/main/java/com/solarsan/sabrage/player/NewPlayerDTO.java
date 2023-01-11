package com.solarsan.sabrage.player;

public record NewPlayerDTO(
        String firstName,
        String lastName,
        String email,
        String pass
) {
}
