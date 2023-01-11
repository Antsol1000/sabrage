import React, {useEffect, useState} from "react";
import {GreatDate, TimeLeft} from "./Dates";
import "../stylesheets/TournamentList.css";
import {Link} from "react-router-dom";
import {getTournaments} from "./Api";

function TournamentListItem(props) {
    const link = "/tournament/" + props.tournament.name;

    return (
        <Link to={link} style={{textDecoration: "none", color: "black"}}>
            <div className="Tournament-list-item">
                <h1>{props.tournament.name}</h1>
                <GreatDate timestamp={props.tournament.time}/>
                <p>organizer: {props.tournament.organizer.first_name} {props.tournament.organizer.last_name}</p>
                <p>discipline: {props.tournament.discipline}</p>
                <TimeLeft timestamp={props.tournament.application_deadline}/>
            </div>
        </Link>
    );
}

function TournamentList() {
    const [tournaments, setTournaments] = useState([]);

    const fetchTournaments = () => {
        getTournaments().then(r => setTournaments(r.data));
    };

    useEffect(() => {
        fetchTournaments();
    }, []);

    return tournaments.map(tournament => {
        return <TournamentListItem tournament={tournament}/>;
    });
}

export default TournamentList;