import React from "react";
import "./App.css";
import {AuthProvider} from "./components/Auth";
import {BrowserRouter, Route} from "react-router-dom";
import TournamentList from "./components/TournamentList";
import Header from "./components/Header";
import Tournament from "./components/Tournament";
import Login from "./components/Login";
import Register from "./components/Register";
import TournamentForm from "./components/TournamentForm";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Header/>
                <Route path="/" exact component={TournamentList}/>
                <Route path="/tournament/:name" component={Tournament}/>
                <Route path="/login" component={Login}/>
                <Route path="/register" component={Register}/>
                <Route path="/organize" component={TournamentForm} />
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
