import {combineReducers} from "redux";
import {certificateList, pagesCount} from "./app/certificates/redux/reducer"
import {
  message,
  refreshToken,
  showStatus,
  token
} from "./app/common/redux/reducer";
import {loginFailureShowStatus} from "./app/login/redux/reducer";

export default combineReducers({
    certificates: combineReducers({
        certificateList,
        pagesCount
    }),
    user: combineReducers({
        token,
        refreshToken,
    }),
    login: combineReducers({
        loginFailureShowStatus
    }),
    alert: combineReducers({
        showStatus,
        message
    })
});