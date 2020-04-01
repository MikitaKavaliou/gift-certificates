import {connect} from 'react-redux'
import {Certificates} from "./CertificatesComponent";
import {fetchCertificates} from "./redux/operations";
import {validateToken} from "../common/redux/operations";

const mapStateToProps = state => {
    return {
        certificates: state.certificates.certificateList,
        token: state.user.token,
        refreshToken: state.user.refreshToken
    }
};
const mapDispatchToProps = dispatch => {
    return {
        fetchCertificates(parameters) {
            dispatch(fetchCertificates(parameters));
        },
        validateToken(token, refreshToken) {
            dispatch(validateToken(token, refreshToken));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Certificates);