import {hideAlert} from "./duck/operations";
import {connect} from "react-redux";
import {Alert} from "./AlertComponent";

const mapStateToProps = state => {
    return {
        alertMessage: state.alert.message
    }
};
const mapDispatchToProps = dispatch => {
    return {
        hideAlert() {
            dispatch(hideAlert());
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Alert)