import {connect} from 'react-redux'
import {Certificates} from "./CertificatesComponent";
import {fetchCertificates} from "./duck/operations";
import {logOut, showAlert} from "../common/duck/operations";

const mapStateToProps = state => {
    return {
        certificates: state.certificates.certificateList,
        certificateStatus: state.certificates.status
    }
};
const mapDispatchToProps = dispatch => {
    return {
        fetchCertificates(parameters) {
            dispatch(fetchCertificates(parameters));
        },
        showAlert(message) {
            dispatch(showAlert(message));
        },
        logOutUser() {
            dispatch(logOut());
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Certificates);