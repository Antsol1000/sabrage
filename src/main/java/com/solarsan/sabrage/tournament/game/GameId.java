package com.solarsan.sabrage.tournament.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameId implements Serializable {

    private int id;
    private String tournament;
}
