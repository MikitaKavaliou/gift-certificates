import {connect} from "react-redux";
import {Pagination} from "./PaginationComponent"

const mapStateToProps = state => {
    return {
        pagesCount: state.certificates.pagesCount
    }
};
export default connect(mapStateToProps)(Pagination)