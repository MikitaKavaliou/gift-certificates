import {connect} from "react-redux";
import {DeleteModal} from "./DeleteModalComponent";
import {updateCertificatesStatus} from "./duck/operations";
import {showAlert} from "../common/duck/operations";

const mapStateToProps = state => {
    return {
        token: state.token,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        updateCertificates() {
            dispatch(updateCertificatesStatus());
        },
        showAlert(message) {
            dispatch(showAlert(message))
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(DeleteModal);