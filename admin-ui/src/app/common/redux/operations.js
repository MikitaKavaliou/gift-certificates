import Actions from "./actions"

export const logOut = () => dispatch => {
    dispatch(Actions.deleteToken());
    dispatch(Actions.deleteRefreshToken())
};

export const hideAlert = () => dispatch => {
    dispatch(Actions.hideAlert());
    dispatch(Actions.deleteAlertMessage());
};

export const validateToken = (token, refreshToken) => async dispatch => {
    try {
        const response = await fetch("https://localhost:8443/api/token?validateAdmin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            }
        });
        if (!response || !response.ok) {
            const response = await fetch("https://localhost:8443/api/token?refresh", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    refreshToken: refreshToken
                })
            });
            if (response && response.ok) {
                const responseBody = await response.json();
                dispatch(Actions.putToken(responseBody.token));
                dispatch(Actions.putRefreshToken(responseBody.refreshToken))
            } else {
                dispatch(Actions.deleteToken());
                dispatch(Actions.deleteRefreshToken());
            }
        }
    } catch (e) {
        dispatch(Actions.putAlertMessage("Server error."));
        dispatch(Actions.showAlert());
    }
};