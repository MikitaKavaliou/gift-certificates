import types from "./types";

export const certificateList = (state = [], action) => {
    switch (action.type) {
        case types.PUT_CERTIFICATES :
            return action.payload;
        default:
            return state;
    }
};

export const pagesCount = (state = null, action) => {
    switch (action.type) {
        case types.PUT_PAGES_COUNT :
            return action.payload;
        default:
            return state;
    }
};

export const status = (state = false, action) => {
    switch (action.type) {
        case types.TRIGGER_CERTIFICATE_UPDATE :
            return !state;
        default:
            return state;
    }
};