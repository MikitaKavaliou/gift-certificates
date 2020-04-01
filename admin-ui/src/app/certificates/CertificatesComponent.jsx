import React, {useEffect, useState} from "react";
import {useHistory, useLocation} from "react-router-dom";
import SearchForm from "./SearchFormContainer";
import CertificatesPagination from "./PaginationContainer";
import Octicon, {TriangleDown, TriangleUp} from "@primer/octicons-react";
import queryString from "query-string";
import DeleteModal from "./DeleteModalContainer";
import CertificateModal from "./CertificateModalContainer";

export function Certificates({token, refreshToken, validateToken, certificates, fetchCertificates, certificateStatus}) {
    const history = useHistory();
    const location = useLocation();
    const [sortField, setSortField] = useState("create_date");
    const [sortType, setSortType] = useState("desc");
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showCertificateModal, setShowCertificateModal] = useState(false);
    const [certificate, setCertificate] = useState({});

    useEffect(() => {
        if (!token) {
            history.push("/login");
        } else {
            validateToken(token, refreshToken);
        }
        fetchCertificates(location.search);
        const queryParams = queryString.parse(location.search);
        if (queryParams.sortField) {
            const urlSortField = queryParams.sortField.toLowerCase();
            if (urlSortField === "create_date" || urlSortField === "last_update_date" || urlSortField === "name") {
                setSortField(urlSortField);
                if (queryParams.sortType) {
                    const urlSortType = queryParams.sortType.toLowerCase();
                    if (urlSortType === "asc" || urlSortType === "desc") {
                        setSortType(urlSortType);
                    }
                }
            }
        }
    }, [fetchCertificates, history, location.search, certificateStatus, token, validateToken, refreshToken]);


    const handleSortClick = e => {
        const chosenSortField = e.currentTarget.id;
        const chosenSortType = !sortType || sortType === "desc" ? "asc" : "desc";
        const queryParameters = queryString.parse(location.search);
        queryParameters.sortField = chosenSortField;
        queryParameters.sortType = chosenSortType;
        history.push(location.pathname + "?" + queryString.stringify(queryParameters));
    };

    const resetSort = () => {
        setSortField("create_date");
        setSortType("desc");
    };

    const handleEditClick = (certificate) => {
        setCertificate(certificate);
        setShowCertificateModal(true);
    };

    const closeCertificateModal = () => {
        setShowCertificateModal(false);
        setCertificate({});
    };

    const handleDeleteClick = (certificate) => {
        setCertificate(certificate);
        setShowDeleteModal(true);
    };

    const closeDeleteModal = () => {
        setShowDeleteModal(false);
        setCertificate({});
    };


    return (
        <main>
            <div className="container-fluid">
                <SearchForm resetSort={resetSort}/>
                <table className="table table-bordered table-striped">
                    <thead className="thead-light">
                    <tr>
                        <th scope="col">#</th>
                        <th id="name" className="clickable-th" scope="col"
                            onClick={handleSortClick}>
                            Name&nbsp;
                            {
                                sortField === "name" && sortType === "desc" &&
                                <Octicon icon={TriangleDown}/>
                            }
                            {
                                (sortField === "name" && sortType === "asc") &&
                                <Octicon icon={TriangleUp}/>
                            }
                        </th>
                        <th scope="col">Tags</th>
                        <th scope="col">Description</th>
                        <th scope="col">Price</th>
                        <th id="create_date" className="clickable-th" scope="col"
                            onClick={handleSortClick}>
                            Created&nbsp;
                            {
                                ((!sortField && !sortType) || (sortField === "create_date"
                                    && sortType === "desc")) &&
                                <Octicon icon={TriangleDown}/>
                            }
                            {
                                (sortField === "create_date" && sortType === "asc") &&
                                <Octicon icon={TriangleUp}/>
                            }
                        </th>
                        <th id="last_update_date" className="clickable-th" scope="col"
                            onClick={handleSortClick}>
                            Updated&nbsp;
                            {
                                sortField === "last_update_date" && sortType === "desc" &&
                                <Octicon icon={TriangleDown}/>
                            }
                            {
                                (sortField === "last_update_date" && sortType === "asc") &&
                                <Octicon icon={TriangleUp}/>
                            }
                        </th>
                        <th scope="col">Duration</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        certificates.map((certificate) =>
                            <tr key={certificate.id}>
                                <th scope="row">{certificate.id}</th>
                                <td>{certificate.name}</td>
                                <td>
                                    {
                                        certificate.tags.map(tag =>
                                            tag !== certificate.tags[certificate.tags.length
                                            - 1] ? tag.name + "," : tag.name)
                                    }
                                </td>
                                <td>{certificate.description}</td>
                                <td>{certificate.price}</td>
                                <td>{certificate.createDate}</td>
                                <td>{certificate.lastUpdateDate}</td>
                                <td>{certificate.duration}</td>
                                <td className="table-actions">
                                    <form className="form-inline">
                                        <div>
                                            <input type="button" className="btn btn-primary"
                                                   onClick={() => handleEditClick(certificate)}
                                                   value="Edit"/>
                                            <input type="button" className="btn btn-danger"
                                                   onClick={() => handleDeleteClick(certificate)}
                                                   value="Delete"/>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        )
                    }
                    </tbody>
                </table>
                <CertificatesPagination/>
            </div>
            <DeleteModal modalState={showDeleteModal} closeModal={closeDeleteModal}
                         certificate={certificate}/>
            <CertificateModal modalState={showCertificateModal} closeModal={closeCertificateModal}
                              certificate={certificate}/>
        </main>
    )
}