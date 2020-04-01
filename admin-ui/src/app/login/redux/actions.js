import types from "./types";

const showLoginFailure = () => ({
    type: types.SHOW_LOGIN_FAILURE
});

const hideLoginFailure = () => ({
    type: types.HIDE_LOGIN_FAILURE
});

export default {
    showLoginFailure,
    hideLoginFailure,
}