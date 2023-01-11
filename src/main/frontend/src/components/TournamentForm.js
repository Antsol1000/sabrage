import React, {Component, useState} from "react";
import "../stylesheets/TournamentForm.css";
import AuthContext from "./Auth";
import {Redirect} from "react-router-dom";
import {organize} from "./Api";

function TournamentForm(props) {
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [discipline, setDiscipline] = useState("");
    const [maxParticipants, setMaxParticipants] = useState("");
    const [time, setTime] = useState("");

    const applicationDeadline = new Date(time);
    applicationDeadline.setHours(applicationDeadline.getHours() - 1);

    const createTournament = () => {
        organize(props.user.name, props.user.pass, name, description, discipline, maxParticipants, time, applicationDeadline)
            .catch(err => alert(err.response.status + " " + err.response.data))
            .then(r => props.after());
    };

    return (
        <div>
            <form className="Tournament-form">
                <div className="Form-elements">
                    <label>Name:</label>
                    <label>Description:</label>
                    <label>Discipline: </label>
                    <label>Max participants:</label>
                    <label>Time:</label>
                </div>

                <div className="Form-elements">
                    <input type="text" onChange={e => setName(e.target.value)}/>
                    <input type="text" onChange={e => setDescription(e.target.value)}/>
                    <select onClick={e => setDiscipline(e.target.value)}>
                        <option disabled selected value> -- select an option --</option>
                        <option value="SABRE">Sabre</option>
                        <option value="EPEE">Epee</option>
                        <option value="FOIL">Foil</option>
                    </select>
                    <input type="text" id="max-participants" onChange={e => setMaxParticipants(e.target.value)}/>
                    <input type="datetime-local" id="time" onChange={e => setTime(e.target.value)}/>
                    <input type="submit" value="Submit" onClick={createTournament}/>
                </div>
            </form>
        </div>
    );
}


class Organize extends Component {
    static contextType = AuthContext;

    state = {
        isCreated: false
    };

    render() {
        if (this.state.isCreated) {
            return <Redirect to="/"/>;
        } else {
            const Auth = this.context;
            return <TournamentForm user={Auth.getUser()} after={() => this.setState({isCrated: true})}/>;
        }
    }
}

export default Organize;