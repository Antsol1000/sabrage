import axios from "axios";

const API_URL = "http://localhost:8081";

export {getTournaments, register, login, apply, getTournament, organize};

function getTournaments() {
    return axios.get(API_URL + "/tournaments");
}

function getTournament(tournament) {
    return axios.get(API_URL + "/tournaments/" + tournament);
}

function login(email, password) {
    return axios.post(API_URL + "/auth", {
        "email": email,
        "password": password
    });
}

function register(firstName, lastName, email, password) {
    return axios.post(API_URL + "/players", {
        "email": email,
        "firstName": firstName,
        "lastName": lastName,
        "pass": password
    });
}

function getAuth(email, password) {
    return {
        username: email,
        password: password
    };
}

function apply(tournament, email, password, license, ranking) {
    const participant = {
        "player": email,
        "license": license,
        "ranking": ranking
    };

    return axios.post(API_URL + "/tournaments/" + tournament + "/participants", participant, {auth: getAuth(email, password)});
}

function organize(email, password, name, description, discipline, maxParticipants, time, applicationDeadline) {
    const tournament = {
        "organizer": email,
        "name": name,
        "description": description,
        "discipline": discipline,
        "maxParticipants": maxParticipants,
        "time": time,
        "applicationDeadline": applicationDeadline
    };

    return axios.post(API_URL + "/tournaments", tournament, {auth: getAuth(email, password)});
}