import {createCertificate, updateCertificate} from "./redux/operations";
import {connect} from "react-redux";
import {CertificateModal} from "./CertificateModalComponent";

const mapStateToProps = state => {
    return {
        token: state.token,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        createCertificate(token, certificate, parameters) {
            dispatch(createCertificate(token, certificate, parameters));
        },
        updateCertificate(token, certificateId, certificate, parameters) {
            dispatch(updateCertificate(token, certificateId, certificate, parameters))
        }
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(CertificateModal);