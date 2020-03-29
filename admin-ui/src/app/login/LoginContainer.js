import {connect} from "react-redux";
import {hideAlert, validateToken} from "../common/redux/operations";
import {logIn} from "./redux/operations";
import LoginComponent from "./LoginComponent";

const mapStateToProps = state => {
    return {
        token: state.token,
        loginFailure: state.login.loginFailureShowStatus
    }
};
const mapDispatchToProps = dispatch => {
    return {
        logIn(credentials) {
            dispatch(logIn(credentials))
        },
        validateToken(token) {
            dispatch(validateToken(token));
        },
        hideAlert() {
            dispatch(hideAlert())
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginComponent);