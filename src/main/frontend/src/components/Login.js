import "../stylesheets/Login.css";
import {Component} from "react";
import AuthContext from "./Auth";
import {Redirect} from "react-router-dom";
import {login} from "./Api";

class Login extends Component {
    static contextType = AuthContext;

    state = {
        email: "",
        password: "",
        isLoggedIn: false,
        isError: false
    };

    componentDidMount() {
        const Auth = this.context;
        const isLoggedIn = Auth.userIsAuthenticated();
        this.setState({isLoggedIn});
    }

    handleSubmit = (e) => {
        e.preventDefault();

        const {email, password} = this.state;
        if (!(email && password)) {
            this.setState({isError: true});
            return;
        }

        login(this.state.email, this.state.password)
            .then(response => {
                const user = {
                    "name": this.state.email,
                    "pass": this.state.password
                };

                const Auth = this.context;
                Auth.userLogin(user);

                this.setState({
                    email: "",
                    password: "",
                    isLoggedIn: true,
                    isError: false
                });
            })
            .catch(error => alert(error.response.status + " " + error.response.data));
    };

    render() {
        if (this.state.isLoggedIn) {
            return <Redirect to="/"/>;
        } else {
            return (
                <div>
                    <form className="Login-form">
                        <div className="Form-elements">
                            <label>Email: </label>
                            <label>Password:</label>
                        </div>

                        <div className="Form-elements">
                            <input type="text" onChange={e => this.setState({"email": e.target.value})}/>
                            <input type="password" onChange={e => this.setState({"password": e.target.value})}/>
                            <input type="submit" value="Sign in" onClick={this.handleSubmit}/>
                        </div>
                    </form>
                </div>
            );
        }
    }

}

export default Login;