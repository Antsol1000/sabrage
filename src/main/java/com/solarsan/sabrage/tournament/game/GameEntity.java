package com.solarsan.sabrage.tournament.game;

import com.solarsan.sabrage.tournament.participant.ParticipantEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(GameId.class)
@Entity
@Table(name = "game")
public class GameEntity {

    @Id
    private int id;

    @Id
    private String tournament;

    @OneToOne
    private ParticipantEntity player1;

    @OneToOne
    private ParticipantEntity player2;

    private Boolean result;

    public GameDTO dto() {
        return new GameDTO(id, player1.dto(), player2.dto(), GameResult.get(result));
    }
}
