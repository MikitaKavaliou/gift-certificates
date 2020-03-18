import React from "react";
import {useHistory} from "react-router-dom";

export function Home() {
    const history = useHistory();
    const handleProceedLogin = () => {
        history.push("/login");
    };
    return (
        <div className="container">
            <div className="row">
                <div className="col-lg-12 text-center">
                    <h1 className="mt-5">Welcome to Admin UI index page.</h1>
                    <p className="lead">
                        Made by Nikita Kovaliov, March 2020.
                        <br/>
                        Proceed to login page.
                    </p>
                    <input className="btn btn-dark" onClick={() => handleProceedLogin()} type="button" value="Proceed to login"/>
                </div>
            </div>
        </div>
    )
}