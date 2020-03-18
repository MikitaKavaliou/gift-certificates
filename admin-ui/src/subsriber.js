import {store} from "./index"

let tokenValue;
export default () => {

    let previousTokenValue = tokenValue;
    tokenValue = store.getState().token;
    if (previousTokenValue !== tokenValue) {
        if (!tokenValue) {
            localStorage.removeItem("token");
        } else {
            localStorage.setItem("token", tokenValue);
        }
    }
}