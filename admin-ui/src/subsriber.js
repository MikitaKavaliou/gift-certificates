import {store} from "./index"

let tokenValue;
let refreshToken;
export default () => {
    let previousTokenValue = tokenValue;
    tokenValue = store.getState().user.token;
    if (previousTokenValue !== tokenValue) {
        if (!tokenValue) {
            localStorage.removeItem("token");
        } else {
            localStorage.setItem("token", tokenValue);
        }
    }
    let previousRefreshTokenValue = refreshToken;
    refreshToken = store.getState().user.refreshToken;
    if (previousRefreshTokenValue !== refreshToken) {
        if (!refreshToken) {
            localStorage.removeItem("refreshToken");
        } else {
            localStorage.setItem("refreshToken", refreshToken);
        }
    }
}