import {connect} from "react-redux";
import {App} from "./AppComponent";

const mapStateToProps = state => {
    return {
        alertShowStatus: state.alert.showStatus,
    }
};

export default connect(mapStateToProps)(App);