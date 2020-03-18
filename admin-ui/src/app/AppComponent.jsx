import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Navbar from "./common/NavbarContainer";
import Certificate from "./certificates/CertificatesContainer";
import Login from "./login/LoginContainer";
import {Footer} from "./common/FooterComponent";
import {PageNotFound} from "./common/PageNotFound";
import Alert from "./common/AlertContainer";
import {Home} from "./home/HomeComponent";

export function App({token, alertShowStatus}) {

    const fetchToken = async () => {
        return token ?
            await fetch("https://localhost:8443/token?admin", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token,
                }
            }) : false;
    };

    return (
        <>
            <Router>
                <Navbar/>
                {
                    alertShowStatus && <Alert/>
                }
                <Switch>
                    <Route exact path="/">
                        <Home/>
                    </Route>
                    <Route exact path="/certificates">
                        <Certificate fetchToken={fetchToken}/>
                    </Route>
                    <Route exact path="/login">
                        <Login fetchToken={fetchToken}/>
                    </Route>
                    <Route>
                        <PageNotFound/>
                    </Route>
                </Switch>
            </Router>
            <Footer/>
        </>);
}