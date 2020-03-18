import {connect} from "react-redux";
import {hideAlert, logIn, logOut, showAlert} from "../common/duck/operations";
import LoginComponent from "./LoginComponent";

const mapStateToProps = state => {
    return {
        token: state.token,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        logInUser(token) {
            dispatch(logIn(token));
        },
        logOutUser() {
            dispatch(logOut());
        },
        showAlert(message) {
            dispatch(showAlert(message))
        },
        hideAlert() {
            dispatch(hideAlert())
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginComponent);