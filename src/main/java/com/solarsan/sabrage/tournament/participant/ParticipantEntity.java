package com.solarsan.sabrage.tournament.participant;

import com.solarsan.sabrage.player.PlayerEntity;
import com.solarsan.sabrage.tournament.TournamentEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class ParticipantEntity {

    @Id
    private UUID id;

    @OneToOne
    private PlayerEntity player;

    private int license;

    private int ranking;

    @ManyToOne
    private TournamentEntity tournament;

    public ParticipantDTO dto() {
        return new ParticipantDTO(id, player.dto(), license, ranking);
    }
}
