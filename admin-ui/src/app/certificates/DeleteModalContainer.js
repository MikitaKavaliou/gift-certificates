import {connect} from "react-redux";
import {DeleteModal} from "./DeleteModalComponent";
import {deleteCertificate} from "./redux/operations";

const mapStateToProps = state => {
    return {
        token: state.token,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        deleteCertificate(token, certificateId, parameters) {
            dispatch(deleteCertificate(token, certificateId, parameters));
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(DeleteModal);