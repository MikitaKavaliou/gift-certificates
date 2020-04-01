import types from "./types";

export const loginFailureShowStatus = (state = false, action) => {
    switch (action.type) {
        case types.SHOW_LOGIN_FAILURE:
            return true;
        case types.HIDE_LOGIN_FAILURE:
            return false;
        default:
            return state;
    }
};