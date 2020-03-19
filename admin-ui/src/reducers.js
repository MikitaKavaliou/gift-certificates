import {combineReducers} from "redux";
import {
    certificateList,
    pagesCount,
    status
} from "./app/certificates/duck/reducer"
import {message, showStatus, token} from "./app/common/duck/reducer";

export default combineReducers({
    certificates: combineReducers({
        certificateList,
        pagesCount,
        status
    }),
    token,
    alert: combineReducers({
        showStatus,
        message
    })
});