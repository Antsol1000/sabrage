import "../stylesheets/Register.css";
import React, {Component} from "react";
import {Redirect} from "react-router-dom";
import {register} from "./Api";

class Register extends Component {
    state = {
        firstName: "",
        lastName: "",
        email: "",
        pass: "",
        isRegistered: false
    };

    handleSubmit = () => {
        register(this.state.firstName, this.state.lastName, this.state.email, this.state.pass)
            .catch((err) => alert(err))
            .then((r) => {
                this.setState({isRegistered: true});
            });
    };

    render() {
        if (this.state.isRegistered) {
            return <Redirect to="/"/>;
        } else {
            return (
                <div>
                    <form className="Register-form">
                        <div className="Form-elements">
                            <label>First name:</label>
                            <label>Last name:</label>
                            <label>Email: </label>
                            <label>Password:</label>
                        </div>

                        <div className="Form-elements">
                            <input type="text" onChange={e => this.setState({firstName: e.target.value})}/>
                            <input type="text" onChange={e => this.setState({lastName: e.target.value})}/>
                            <input type="text" onChange={e => this.setState({email: e.target.value})}/>
                            <input type="password" onChange={e => this.setState({pass: e.target.value})}/>
                            <input type="submit" value="Register" onClick={this.handleSubmit}/>
                        </div>
                    </form>
                </div>
            );
        }
    }
}

export default Register;