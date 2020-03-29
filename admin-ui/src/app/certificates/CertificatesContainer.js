import {connect} from 'react-redux'
import {Certificates} from "./CertificatesComponent";
import {fetchCertificates} from "./redux/operations";
import {validateToken} from "../common/redux/operations";

const mapStateToProps = state => {
    return {
        certificates: state.certificates.certificateList,
        token: state.token
    }
};
const mapDispatchToProps = dispatch => {
    return {
        fetchCertificates(parameters) {
            dispatch(fetchCertificates(parameters));
        },
        validateToken(token) {
            dispatch(validateToken(token));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Certificates);