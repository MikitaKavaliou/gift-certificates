import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import * as JWT from "jwt-decode";

export default function LoginComponent({fetchToken, logInUser, logOutUser, showAlert, hideAlert}) {
    const [usernameInput, setUsername] = useState("");
    const [passwordInput, setPassword] = useState("");
    const [wasUsernameTouched, setWasUsernameTouched] = useState(false);
    const [wasPasswordTouched, setWasPasswordTouched] = useState(false);
    const [wasUsernameOnBlur, setWasUsernameOnBlur] = useState(false);
    const [wasPasswordOnBlur, setWasPasswordOnBlur] = useState(false);
    const [wasSubmittedIncorrectData, setWasSubmittedIncorrectData] = useState(false);
    const history = useHistory();

    useEffect(() => {
        (async () => {
            try {
                const response = await fetchToken();
                if (response && response.ok) {
                    history.push("/certificates");
                } else {
                    logOutUser();
                }
            } catch (e) {
                showAlert("Server not responding.");
            }
        })();
    }, [fetchToken, history, logOutUser, showAlert]);

    const handleSubmit = async e => {
        try {
            e.preventDefault();
            hideAlert();
            const response = await fetch("https://localhost:8443/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }, body: JSON.stringify({
                    username: usernameInput,
                    password: passwordInput
                })
            });
            const responseBody = await response.json();
            if (response.ok && (JWT(responseBody.token).role === "ADMIN")) {
                logInUser(responseBody.token);
                history.push("/certificates");
            } else if (response.status === 401 || (response.ok && (JWT(responseBody.token).role === "USER"))) {
                setWasSubmittedIncorrectData(true);
            } else {
                let responseBody = await response.json();
                showAlert(responseBody.errorMessage);
            }
        } catch (e) {
            showAlert("Server not responding.");
        }
    };

    const handleClick = e => {
        setWasSubmittedIncorrectData(false);
        e.target.name === "username" ? setWasUsernameTouched(true) : setWasPasswordTouched(true);
    };

    const handleChange = e => {
        setWasSubmittedIncorrectData(false);
        e.target.name === "username" ? setUsername(e.target.value) : setPassword(e.target.value);
    };

    const handleBlur = e => {
        (e.target.name === "username") ? setWasUsernameOnBlur(true) : setWasPasswordOnBlur(true);
    };

    const formValid = (() => usernameInput?.length >= 3 && usernameInput.length <= 30 &&
        passwordInput?.length >= 4 && usernameInput.length <= 40)();

    return (
        <main>
            <form onSubmit={handleSubmit}>
                <div className="login-container">
                    <h1 className="login-header">LogIn</h1>
                    <input
                        type="text"
                        placeholder="Username"
                        name="username"
                        onClick={handleClick}
                        onBlur={handleBlur}
                        onChange={handleChange}
                    />
                    <div className="error">
                        {wasUsernameTouched && wasUsernameOnBlur &&
                        (usernameInput.length < 3 ?
                            "Minimum 3 characters is required."
                            : usernameInput.length >= 30
                            && "Maximum length is 30 characters")
                        }
                    </div>
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        onClick={handleClick}
                        onBlur={handleBlur}
                        onChange={handleChange}
                    />
                    <div className="error">
                        {wasPasswordTouched && wasPasswordOnBlur &&
                        (passwordInput.length < 4 ?
                            "Minimum 4 characters is required." :
                            passwordInput.length >= 30 && "Maximum length is 30 characters")
                        }
                    </div>
                    <div className="error">
                        <small>{wasSubmittedIncorrectData &&
                        <div className="error">Wrong username or password</div>}</small>
                    </div>
                    <input className="btn btn-primary" disabled={!formValid}
                           type="submit" value="Login"/>
                </div>
            </form>
        </main>
    )
}