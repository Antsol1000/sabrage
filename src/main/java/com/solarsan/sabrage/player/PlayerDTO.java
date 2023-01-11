package com.solarsan.sabrage.player;

public record PlayerDTO(
        String firstName,
        String lastName,
        String email,
        boolean active
) {
}
