import types from "./types";

const putToken = value => ({
    type: types.PUT_TOKEN,
    payload: value
});

const deleteToken = () => ({
    type: types.DELETE_TOKEN
});

const putRefreshToken = value => ({
    type: types.PUT_REFRESH_TOKEN,
    payload: value
});

const deleteRefreshToken = () => ({
    type: types.DELETE_REFRESH_TOKEN
});

const showAlert = () => ({
    type: types.SHOW_ALERT
});

const hideAlert = () => ({
    type: types.HIDE_ALERT
});

const putAlertMessage = value => ({
    type: types.PUT_ALERT_MESSAGE,
    payload: value
});

const deleteAlertMessage = () => ({
    type: types.DELETE_ALERT_MESSAGE
});
export default {
    putToken,
    deleteToken,
    putRefreshToken,
    deleteRefreshToken,
    showAlert,
    hideAlert,
    putAlertMessage,
    deleteAlertMessage
}