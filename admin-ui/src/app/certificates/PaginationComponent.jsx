import {useHistory, useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import queryString from "query-string";

export function Pagination({pagesCount}) {
    const history = useHistory();
    const location = useLocation();
    const [perPage, setPerPage] = useState(10);

    useEffect(() => {
        const queryParams = queryString.parse(location.search);
        setPerPage(queryParams.perPage ? parseInt(queryParams.perPage) : 10);
    }, [location.search]);

    const handlePageClick = e => {
        const queryParams = queryString.parse(location.search);
        queryParams.page = e.target.value;
        history.push(location.pathname + "?" + queryString.stringify(queryParams));
    };

    const handlePerPageChange = e => {
        const queryParams = queryString.parse(location.search);
        queryParams.page = 1;
        queryParams.perPage = e.target.value;
        history.push(location.pathname + "?" + queryString.stringify(queryParams));
    };

    const pages = (() => {
        const queryParams = queryString.parse(location.search);
        const page = queryParams.page ? parseInt(queryParams.page) : 1;
        const items = [];
        let key = 1;
        if (pagesCount > 1) {
            if (pagesCount <= 8) {
                for (let i = 1; i <= pagesCount; i++) {
                    items.push(
                        <li key={key++}
                            className={i !== page ? "page-item" : "page-item active"}>
                            <input type="button" value={i}
                                   onClick={handlePageClick}
                                   className="page-link"/>
                        </li>);
                }
            } else {
                if (page - 1 > 3) {
                    items.push(
                        <li key={key++} className="page-item">
                            <input type="button" value="1"
                                   onClick={handlePageClick}
                                   className="page-link"/>
                        </li>);
                    items.push(
                        <li key={key++} className="page-item">
                            <input disabled type="button" value="..."
                                   onClick={handlePageClick}
                                   className="page-link"/>
                        </li>);
                    for (let i = page - 2; i <= page; i++) {
                        items.push(
                            <li key={key++}
                                className={i !== page ? "page-item" : "page-item active"}>
                                <input type="button" value={i}
                                       onClick={handlePageClick}
                                       className="page-link"/>
                            </li>);
                    }

                } else {
                    for (let i = 1; i <= page; i++) {
                        items.push(
                            <li key={key++}
                                className={i !== page ? "page-item" : "page-item active"}>
                                <input type="button" value={i}
                                       onClick={handlePageClick}
                                       className="page-link"/>
                            </li>);
                    }
                }
                if (pagesCount - page <= 3) {
                    for (let i = page + 1; i <= pagesCount; i++) {
                        items.push(
                            <li key={key++}
                                className={i !== page ? "page-item" : "page-item active"}>
                                <input type="button" value={i}
                                       onClick={handlePageClick}
                                       className="page-link"/>
                            </li>);
                    }
                } else {
                    for (let i = page + 1; i <= page + 2; i++) {
                        items.push(
                            <li key={key++}
                                className={i !== page ? "page-item" : "page-item active"}>
                                <input type="button" value={i}
                                       onClick={handlePageClick}
                                       className="page-link"/>
                            </li>);
                    }
                    items.push(
                        <li key={key++} className="page-item">
                            <input disabled type="button" value="..."
                                   onClick={handlePageClick}
                                   className="page-link"/>
                        </li>);
                    items.push(
                        <li key={key++} className="page-item">
                            <input type="button" value={pagesCount}
                                   onClick={handlePageClick}
                                   className="page-link"/>
                        </li>);
                }
            }
        }
        return items;
    })();
    return (
        <>
            <form className="line-form justify-content-center">
                <nav aria-label="Page navigation example">
                    <ul className="pagination">
                        {pages}
                    </ul>
                </nav>
                <div>
                    <select className="form-control page-link per-page" value={perPage}
                            onChange={handlePerPageChange}
                            id="perPage">
                        <option>10</option>
                        <option>20</option>
                        <option>50</option>
                    </select>
                </div>
            </form>
        </>
    )
}