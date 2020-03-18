import React from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

export function DeleteModal({modalState, closeModal, certificate, token, updateCertificates, showAlert}) {
    const onDeleteClick = async () => {
        try {
            const response = await fetch("https://localhost:8443/certificates/" + certificate.id, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });
            if (!response.ok) {
                let responseBody = await response.json();
                showAlert(responseBody.errorMessage);
            }
        } catch (e) {
            showAlert("Server not responding.");
        } finally {
            closeModal();
            updateCertificates();
        }
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