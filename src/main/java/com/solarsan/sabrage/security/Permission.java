package com.solarsan.sabrage.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    TOURNAMENT_CREATE("tournament:create"),
    TOURNAMENT_UPDATE("tournament:update"),
    TOURNAMENT_APPLY("tournament:apply"),
    TOURNAMENT_SET_RESULT("tournament:setResult"),
    PLAYER_GET_ALL("player:getAll"),
    PLAYER_ACTIVATE("player:activate");

    private final String permission;
}
