import types from "./types";

export const token = (state = null, action) => {
    switch (action.type) {
        case types.PUT_TOKEN :
            return action.payload;
        case types.DELETE_TOKEN:
            return null;
        default:
            return state;
    }
};

export const showStatus = (state = false, action) => {
    switch (action.type) {
        case types.SHOW_ALERT:
            return true;
        case types.HIDE_ALERT:
            return false;
        default:
            return state;
    }
};

export const message = (state = null, action) => {
    switch (action.type) {
        case types.PUT_ALERT_MESSAGE:
            return action.payload;
        case types.DELETE_ALERT_MESSAGE:
            return null;
        default:
            return state;
    }
};