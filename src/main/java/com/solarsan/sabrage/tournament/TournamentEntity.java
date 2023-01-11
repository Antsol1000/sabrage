package com.solarsan.sabrage.tournament;

import com.solarsan.sabrage.player.PlayerEntity;
import com.solarsan.sabrage.tournament.participant.ParticipantEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "tournament")
public class TournamentEntity {

    @Id
    private String name;

    @ManyToOne
    private PlayerEntity organizer;

    private String description;

    private Discipline discipline;

    private Timestamp time;

    private Timestamp applicationDeadline;

    private int maxParticipants;

    @ManyToOne
    private ParticipantEntity winner;

}
