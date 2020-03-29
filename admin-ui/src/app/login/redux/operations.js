import * as JWT from "jwt-decode";
import CommonActions from "../../common/redux/actions";
import Actions from "./actions";

export const logIn = (credentials) => async dispatch => {
    try {
        dispatch(Actions.hideLoginFailure());
        const response = await fetch("https://localhost:8443/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }, body: JSON.stringify(credentials)
        });
        const responseBody = await response.json();
        if (response.ok && (JWT(responseBody.token).role === "ADMIN")) {
            dispatch(CommonActions.putToken(responseBody.token));
        } else {
            dispatch(Actions.showLoginFailure());
        }
    } catch (e) {
        dispatch(CommonActions.putAlertMessage("Server error."));
        dispatch(CommonActions.showAlert());
    }
};