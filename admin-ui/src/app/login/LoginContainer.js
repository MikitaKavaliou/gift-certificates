import {connect} from "react-redux";
import {hideAlert, validateToken} from "../common/redux/operations";
import {logIn} from "./redux/operations";
import LoginComponent from "./LoginComponent";

const mapStateToProps = state => {
    return {
        token: state.user.token,
        loginFailure: state.login.loginFailureShowStatus,
        refreshToken: state.user.refreshToken
    }
};
const mapDispatchToProps = dispatch => {
    return {
        logIn(credentials) {
            dispatch(logIn(credentials))
        },
        validateToken(token, refreshToken) {
            dispatch(validateToken(token, refreshToken));
        },
        hideAlert() {
            dispatch(hideAlert())
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginComponent);