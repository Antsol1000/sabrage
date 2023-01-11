import React from "react";
import "../stylesheets/Header.css";
import logo from "../img/logo.png";
import {Link} from "react-router-dom";
import {useAuth} from "./Auth";

function LoginButton(props) {
    if (props.show) {
        return (
            <Link to="/login" style={{"margin-left": "auto"}}>
                <button id="Login-button">Log in</button>
            </Link>
        );
    }
}

function RegisterButton(props) {
    if (props.show) {
        return (
            <Link to="/register" style={{"margin-right": "20px"}}>
                <button id="Register-button">Sign up</button>
            </Link>
        );
    }
}

function OrganizeButton(props) {
    if (props.show) {
        return (
            <Link to="/organize" style={{"margin-left": "auto"}}>
                <button id="Organize-button">Organize</button>
            </Link>
        );
    }
}

function LogoutButton(props) {
    if (props.show) {
        return (
            <Link to="/" onClick={props.logout} style={{"margin-right": "20px"}}>
                <button id="Logout-button">Log out</button>
            </Link>
        );
    }
}

function HelloBadge(props) {
    if (props.show) {
        return (
            <p className="Hello">Hello, {props.getName()}.</p>
        );
    }
}

function Header() {
    const {getUser, userIsAuthenticated, userLogout} = useAuth();

    return (
        <header className="App-header">
            <Link exact="true" to="/">
                <img src={logo} className="App-logo" alt="logo"/>
            </Link>
            <p>Sabrage</p>
            <HelloBadge show={userIsAuthenticated()} getName={() => getUser().name}/>
            <LoginButton show={!userIsAuthenticated()}/>
            <RegisterButton show={!userIsAuthenticated()}/>
            <OrganizeButton show={userIsAuthenticated()}/>
            <LogoutButton show={userIsAuthenticated()} logout={userLogout}/>
        </header>
    );
}

export default Header;