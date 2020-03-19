import {updateCertificatesStatus} from "./duck/operations";
import {showAlert} from "../common/duck/operations";
import {connect} from "react-redux";
import {CertificateModal} from "./CertificateModalComponent";

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
export default connect(mapStateToProps, mapDispatchToProps)(CertificateModal);