import Actions from "./actions"

export const logOut = Actions.deleteToken;

export const showAlert = message => dispatch => {
    dispatch(Actions.putAlertMessage(message));
    dispatch(Actions.showAlert());
};

export const hideAlert = () => dispatch => {
    dispatch(Actions.hideAlert());
    dispatch(Actions.deleteAlertMessage());
};

export const validateToken = token => async dispatch => {
    try {
        const response = await fetch("https://localhost:8443/api/token?validateAdmin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            }
        });
        if (!response || !response.ok) {
            dispatch(Actions.deleteToken());
        }
    } catch (e) {
        dispatch(Actions.putAlertMessage("Server error."));
        dispatch(Actions.showAlert());
    }
};