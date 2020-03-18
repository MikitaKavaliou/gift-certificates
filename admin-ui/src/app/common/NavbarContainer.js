import {logOut} from "./duck/operations";
import {connect} from "react-redux";
import {Navbar} from "./NavbarComponent";

const mapStateToProps = state => {
    return {
        token: state.token
    }
};
const mapDispatchToProps = dispatch => {
    return {
        logOutUser() {
            dispatch(logOut());
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Navbar)