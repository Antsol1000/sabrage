package com.solarsan.sabrage.tournament.game;

public enum GameResult {
    PLAYER_1, PLAYER_2, NOT_FINISHED;

    public static GameResult get(final Boolean result) {
        if (result == null) return GameResult.NOT_FINISHED;
        else if (result) return GameResult.PLAYER_1;
        return GameResult.PLAYER_2;
    }

    public Boolean get() {
        switch (this) {
            case PLAYER_1 -> {
                return true;
            }
            case PLAYER_2 -> {
                return false;
            }
            case NOT_FINISHED -> {
                return null;
            }
        }
        return null;
    }
}
