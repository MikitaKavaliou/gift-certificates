import {hideAlert} from "../common/redux/operations";
import {connect} from "react-redux";
import {SearchForm} from "./SearchFormComponent";

const mapDispatchToProps = dispatch => {
    return {
        hideAlert() {
            dispatch(hideAlert())
        },
    }
};

export default connect(null, mapDispatchToProps)(SearchForm);