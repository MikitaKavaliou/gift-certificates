import {logOut} from "./redux/operations";
import {connect} from "react-redux";
import {Navbar} from "./NavbarComponent";

const mapStateToProps = state => {
    return {
        token: state.user.token
    }
};
const mapDispatchToProps = dispatch => {
    return {
        logOut() {
            dispatch(logOut());
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Navbar)