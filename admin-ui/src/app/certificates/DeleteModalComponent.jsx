import React from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import {useLocation} from "react-router-dom";

export function DeleteModal({modalState, closeModal, certificate, token, deleteCertificate}) {
    const location = useLocation();

    const onDeleteClick = () => {
        deleteCertificate(token, certificate.id, location.search);
        closeModal();
    };

    return (
        <Modal centered show={modalState} onHide={closeModal}>
            <Modal.Header closeButton>
                <Modal.Title>Delete confirmation</Modal.Title>
            </Modal.Header>
            <Modal.Body>Do you really want to delete certificate with
                id = {certificate.id} ?</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={closeModal}>
                    Cancel
                </Button>
                <Button variant="danger" onClick={() => onDeleteClick()}>
                    Delete
                </Button>
            </Modal.Footer>
        </Modal>
    )
}