import "../stylesheets/Tournament.css";
import React, {Component, useEffect, useState} from "react";
import {GreatDate, TimeLeft} from "./Dates";
import AuthContext from "./Auth";
import {useParams} from "react-router-dom";
import {apply, getTournament} from "./Api";

function Winner(props) {
    if (props.winner) {
        return (
            <h1> Winner: {props.winner.player.first_name} {props.winner.player.last_name} </h1>
        );
    }
}

function Participant(props) {
    return (
        <tr>
            <td>{props.participant.ranking}</td>
            <td>{props.participant.player.first_name} {props.participant.player.last_name}</td>
            <td>{props.participant.license}</td>
        </tr>
    );
}

function ParticipantTable(props) {
    const participants = props.participants;

    if (!participants) return;

    participants.sort((a, b) => b.ranking - a.ranking);

    return (
        <table>
            <tr>
                <th>Ranking</th>
                <th>Name</th>
                <th>License</th>
            </tr>
            {participants.map(participant => <Participant participant={participant}/>)}
        </table>
    );
}

function Game(props) {
    return (
        <tr>
            <td>{props.game.id}</td>
            <td>{props.game.player1.player.first_name} {props.game.player1.player.last_name}</td>
            <td>{props.game.player2.player.first_name} {props.game.player2.player.last_name}</td>
            <td>{props.game.result}</td>
        </tr>
    );
}

function GameTable(props) {
    const games = props.games;

    if (!games) return;

    games.sort((a, b) => b.id - a.id);

    return (
        <table>
            <tr>
                <th>Id</th>
                <th>Player 1</th>
                <th>Player 2</th>
                <th>Result</th>
            </tr>
            {games.map(game => <Game game={game}/>)}
        </table>
    );
}

function submitApply(tournament, user) {
    apply(tournament, user.name, user.pass, 56781234, 1000) // TODO
        .then(r => alert("Successful application, please reload the page."))
        .catch(err => alert(err.response.data));
}

function ApplyButton(props) {
    if (props.user) {
        return (
            <button className="Apply-button" onClick={() => submitApply(props.tournament, props.user)}>Apply</button>
        );
    }
}

function TournamentDetails(props) {
    const [tournament, setTournament] = useState([]);

    const fetchTournament = () => {
        getTournament(props.name).then(r => setTournament(r.data));
    };

    useEffect(() => {
        fetchTournament();
    }, []);

    return [tournament].map(tournament => {
        return (<div>
            <div className="Tournament-main-info">
                <h1>{tournament.name}</h1>
                <GreatDate timestamp={tournament.time}/>
            </div>
            <div className="Tournament-layout">

                <div className="Tournament-info">
                    <Winner winner={tournament.winner}/>
                    <TimeLeft timestamp={tournament.application_deadline}/>
                    <h3>Discipline: {tournament.discipline}</h3>
                    <h3>Description:</h3>
                    <p>{tournament.description}</p>
                </div>

                <div className="Tournament-participants">
                    <h2>Participants (limit: {tournament.max_participants})</h2>
                    <ParticipantTable participants={tournament.participants}/>
                    <br/>
                    <ApplyButton tournament={tournament.name} user={props.user}/>
                </div>

                <div className="Tournament-games">
                    <h2>Games:</h2>
                    <GameTable games={tournament.games}/>
                </div>
            </div>
        </div>);
    });
}

function withParams(Component) {
    return props => <Component {...props} params={useParams()}/>;
}

class Tournament extends Component {
    static contextType = AuthContext;
    state = {
        tournamentName: ""
    };

    componentDidMount() {
        const {name: tr} = this.props.params;
        this.setState({
            "tournamentName": tr
        });
    }

    render() {
        if (this.state.tournamentName === "") {
            return <p>loading...</p>;
        } else {
            const Auth = this.context;
            return <TournamentDetails name={this.state.tournamentName} user={Auth.getUser()}/>;
        }
    }
}

export default withParams(Tournament);