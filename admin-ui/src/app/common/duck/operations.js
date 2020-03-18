import Actions from "./actions"

export const logIn = Actions.putToken;
export const logOut = Actions.deleteToken;

export const showAlert = message => dispatch => {
    dispatch(Actions.putAlertMessage(message));
    dispatch(Actions.showAlert());
};

export const hideAlert = () => dispatch => {
    dispatch(Actions.hideAlert());
    dispatch(Actions.deleteAlertMessage());
};