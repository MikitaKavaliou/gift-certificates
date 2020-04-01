import React, {useState} from "react";
import {useHistory, useLocation} from "react-router-dom";
import JWT from "jwt-decode"
import CertificateModal from "../certificates/CertificateModalContainer";

export function Navbar({logOut, token}) {
    const [showCertificateModal, setShowCertificateModal] = useState(false);
    const username = token ? JWT(token).username : null;
    const history = useHistory();
    const pathName = useLocation().pathname;

    const handleAddClick = () => {
        setShowCertificateModal(true);
    };

    const closeCertificateModal = () => {
        setShowCertificateModal(false);
    };

    const handleLogOutButton = () => {
        logOut();
        history.push("/login");
    };
    return (
        <>
            <nav className="navbar navbar-dark navbar navbar-dark bg-dark">
                <div className="navbar-brand">
                    Admin UI
                </div>
                {
                    token && pathName === "/certificates" &&
                    <input type="button" className="btn btn-primary" onClick={() => handleAddClick()} value="Add new"/>
                }
                {
                    token &&
                    <form className="form-inline">
                        <span className="navbar-text">{username}&nbsp;</span>
                        <input className="btn btn-dark" onClick={handleLogOutButton} type="button" value="Logout"/>
                    </form>
                }
            </nav>
            <CertificateModal modalState={showCertificateModal} closeModal={closeCertificateModal} certificate={null}/>
        </>
    )
}