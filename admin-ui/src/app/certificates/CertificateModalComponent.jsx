import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import React, {useEffect, useState} from "react";
import Form from "react-bootstrap/Form";
import ListGroup from "react-bootstrap/ListGroup";
import {Container} from "react-bootstrap";
import Octicon, {X} from "@primer/octicons-react";

export function CertificateModal({modalState, closeModal, certificate, token, updateCertificates, showAlert}) {
    const [tags, setTags] = useState([]);
    const [nameInput, setNameInput] = useState("");
    const [descriptionInput, setDescriptionInput] = useState("");
    const [priceInput, setPriceInput] = useState("");
    const [durationInput, setDurationInput] = useState("");
    const [tagInput, setTagInput] = useState("");
    const [wasNameTouched, setWasNameTouched] = useState(false);
    const [wasNameOnBlur, setWasNameOnBlur] = useState(false);
    const [wasDescriptionTouched, setWasDescriptionTouched] = useState(false);
    const [wasDescriptionOnBlur, setWasDescriptionOnBlur] = useState(false);
    const [wasDurationTouched, setWasDurationTouched] = useState(false);
    const [wasDurationOnBlur, setWasDurationOnBlur] = useState(false);
    const [wasPriceTouched, setWasPriceTouched] = useState(false);
    const [wasPriceOnBlur, setWasPriceOnBlur] = useState(false);
    const [tagErrorMessage, setTagErrorMessage] = useState(null);

    useEffect(() => {
        if (certificate) {
            certificate.tags ? setTags(certificate.tags) : setTags([]);
            certificate.name ? setNameInput(certificate.name) : setNameInput("");
            certificate.description ? setDescriptionInput(certificate.description) : setDescriptionInput("");
            certificate.duration ? setDurationInput(String(certificate.duration)) : setDurationInput("");
            certificate.price ? setPriceInput(String(certificate.price)) : setPriceInput("");
        }
        setTagErrorMessage(null);
        setTagInput("");
        if (certificate) {
            setWasNameOnBlur(true);
            setWasDescriptionOnBlur(true);
            setWasDurationOnBlur(true);
            setWasPriceOnBlur(true);
            setWasNameTouched(true);
            setWasDescriptionTouched(true);
            setWasDurationTouched(true);
            setWasPriceTouched(true);
        } else {
            setWasNameOnBlur(false);
            setWasDescriptionOnBlur(false);
            setWasDurationOnBlur(false);
            setWasPriceOnBlur(false);
            setWasNameTouched(false);
            setWasDescriptionTouched(false);
            setWasDurationTouched(false);
            setWasPriceTouched(false);
        }


    }, [certificate]);

    const handleInputClick = e => {
        if (e.target.name === "nameInput") {
            setWasNameTouched(true);
        } else if (e.target.name === "descriptionInput") {
            setWasDescriptionTouched(true);
        } else if (e.target.name === "durationInput") {
            setWasDurationTouched(true);
        } else {
            setWasPriceTouched(true);
        }
    };

    const handleBlur = e => {
        if (e.target.name === "nameInput") {
            setWasNameOnBlur(true);
        } else if (e.target.name === "descriptionInput") {
            setWasDescriptionOnBlur(true);
        } else if (e.target.name === "durationInput") {
            setWasDurationOnBlur(true);
        } else {
            setWasPriceOnBlur(true);
        }
    };

    const handleInputChange = e => {
        if (e.target.name === "nameInput") {
            setWasNameTouched(true);
            setNameInput(e.target.value)
        } else if (e.target.name === "descriptionInput") {
            setWasDescriptionTouched(true);
            setDescriptionInput(e.target.value);
        } else if (e.target.name === "durationInput") {
            setWasDurationTouched(true);
            setDurationInput(e.target.value);
        } else if (e.target.name === "priceInput") {
            setWasPriceTouched(true);
            setPriceInput(e.target.value);
        } else {
            setTagInput(e.target.value);
            setTagErrorMessage(null);
        }
    };

    const handlePriceKeyPress = e => {
        if (e.key !== "Backspace" && e.key !== "Delete" && e.key !== "ArrowLeft" && e.key !== "ArrowRight") {
            if ((priceInput.includes(".") && e.key.charCodeAt(0) === 46)
                || (!priceInput.includes(".") && priceInput.length > 3 && e.key.charCodeAt(0)
                    !== 46)
                || (priceInput.includes(".") && priceInput.split(".")[1].length > 1
                    && e.target.selectionStart > priceInput.indexOf("."))
                || (priceInput.includes(".") && priceInput.split(".")[0].length > 3
                    && e.target.selectionStart <= priceInput.indexOf("."))
                || ((e.key.charCodeAt(0) < 48 || e.key.charCodeAt(0) > 57)
                    && e.key.charCodeAt(0) !== 46)) {
                e.preventDefault();
            }
        }
    };

    const handleDurationKeyPress = e => {
        if (e.key !== "Backspace" && e.key !== "Delete" && e.key !== "ArrowLeft" && e.key !== "ArrowRight") {
            if (e.key.charCodeAt(0) < 48 || e.key.charCodeAt(0) > 57 || durationInput.length > 3) {
                e.preventDefault();
            }
        }
    };

    const handleTagKeyPress = e => {
        const value = e.target.value;
        let isDuplicate = false;
        for (let tag of tags) {
            if (tag.name === value) {
                isDuplicate = true;
            }
        }
        if (e.key === "Enter" && value.length >= 3 && value.length <= 15 && !isDuplicate) {
            setTags([...tags, {name: value}]);
            setTagInput("");
            setTagErrorMessage(null);
        } else if ((value.length < 3 && e.key === "Enter") || value.length === 15) {
            e.preventDefault();
            setTagErrorMessage("Min length is 3. Max length is 15.");
        } else if (isDuplicate && e.key === "Enter") {
            e.preventDefault();
            setTagErrorMessage("Such tag already exists.");
        }
    };

    const handleTagRemove = i => {
        setTags(tags.filter((tag, index) => i !== index));
    };

    const tagList = (() => {
        let items = [];
        tags.map((tag, index) =>
            items.push(
                <ListGroup.Item className="input-tags" key={index}>{tag.name}&nbsp;
                    <Container className="icon-container" onClick={() => handleTagRemove(index)}>
                        <Octicon icon={X}/>
                    </Container>
                </ListGroup.Item>)
        );
        return items;
    })();

    const isCertificateNotChanged = () => {
        return (certificate ? certificate.name === nameInput && certificate.description === descriptionInput &&
            certificate.price === parseFloat(priceInput) && certificate.duration === parseInt(durationInput) &&
            JSON.stringify(certificate.tags) === JSON.stringify(tags) : true);
    };

    const formValid = (() => nameInput?.length >= 5 && nameInput.length <= 30 &&
            descriptionInput?.length >= 12 && descriptionInput.length <= 1000 &&
            !isNaN(parseFloat(priceInput)) && parseFloat(priceInput) >= 0 &&
            !isNaN(parseInt(durationInput)) && parseInt(durationInput) >= 0 &&
            tagInput === "" && !isCertificateNotChanged()
    )();

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            certificate ? await handleUpdate() : await handleCreate();
        } catch (e) {
            showAlert("Server not responding");
        } finally {
            closeModal();
            updateCertificates();
        }
    };

    const handleCreate = async () => {
        const response = await fetch("https://localhost:8443/certificates", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }, body: JSON.stringify({
                name: nameInput,
                description: descriptionInput,
                price: parseFloat(priceInput),
                duration: parseInt(durationInput),
                tags: tags
            })
        });
        if (!response.ok) {
            let responseBody = await response.json();
            showAlert(responseBody.errorMessage);
        }
    };

    const handleUpdate = async () => {
        const tagsForAdding = getTagsForAdding();
        const tagsForDeletion = getTagsForDeletion();
        const response = await fetch("https://localhost:8443/certificates/" + certificate.id, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }, body: JSON.stringify({
                name: nameInput,
                description: descriptionInput,
                price: parseFloat(priceInput),
                duration: parseInt(durationInput),
                tagsForAdding: tagsForAdding,
                tagsForDeletion: tagsForDeletion,
            })
        });
        if (!response.ok) {
            let responseBody = await response.json();
            showAlert(responseBody.errorMessage);
        }
    };

    const getTagsForAdding = () => {
        const tagsForAdding = [];
        for (let resultTag of tags) {
            let addFlag = true;
            for (let sourceTag of certificate.tags) {
                if (sourceTag.name === resultTag.name) {
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                tagsForAdding.push(resultTag);
            }
        }
        return tagsForAdding;
    };


    const getTagsForDeletion = () => {
        const tagsForDeletion = [];
        for (let sourceTag of certificate.tags) {
            let deleteFlag = true;
            for (let resultTag of tags) {
                if (sourceTag.name === resultTag.name) {
                    deleteFlag = false;
                    break;
                }
            }
            if (deleteFlag) {
                tagsForDeletion.push(sourceTag);
            }
        }
        return tagsForDeletion;
    };

    return (
        <>
            <Modal centered show={modalState} onHide={closeModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{certificate ? "Edit certificate with id = " + certificate.id : "Add new certificate"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="nameForm">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text"
                                          name="nameInput"
                                          placeholder="Certificate name"
                                          value={nameInput}
                                          onChange={handleInputChange}
                                          onClick={handleInputClick}
                                          onBlur={handleBlur}
                            />
                            <Container className="error">
                                {
                                    wasNameTouched && wasNameOnBlur &&
                                    (nameInput.length < 6 ?
                                        "Minimum 6 characters is required."
                                        : nameInput.length > 30
                                        && "Maximum length is 30 characters")
                                }
                            </Container>
                        </Form.Group>
                        <Form.Group controlId="descriptionForm">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea"
                                          rows="3"
                                          name="descriptionInput"
                                          placeholder="Certificate description"
                                          value={descriptionInput}
                                          onChange={handleInputChange}
                                          onClick={handleInputClick}
                                          onBlur={handleBlur}
                            />
                            <Container className="error">
                                {
                                    wasDescriptionTouched && wasDescriptionOnBlur &&
                                    (descriptionInput.length < 12 ?
                                        "Minimum 12 characters is required."
                                        : descriptionInput.length > 1000
                                        && "Maximum length is 1000 characters")
                                }
                            </Container>
                        </Form.Group>
                        <Form.Group controlId="durationForm">
                            <Form.Label>Duration</Form.Label>
                            <Form.Control type="text"
                                          name="durationInput"
                                          placeholder="Certificate duration"
                                          value={durationInput}
                                          onChange={handleInputChange}
                                          onClick={handleInputClick}
                                          onBlur={handleBlur}
                                          onKeyDown={handleDurationKeyPress}
                            />
                        </Form.Group>
                        <Container className="error">
                            {
                                wasDurationTouched && wasDurationOnBlur
                                && (parseInt(durationInput) < 0 || isNaN(parseInt(durationInput)))
                                && "Must be an integer number and not less than 0."
                            }
                        </Container>
                        <Form.Group controlId="priceForm">
                            <Form.Label>Price</Form.Label>
                            <Form.Control type="text"
                                          name="priceInput"
                                          placeholder="Certificate price"
                                          value={priceInput}
                                          onChange={handleInputChange}
                                          onClick={handleInputClick}
                                          onBlur={handleBlur}
                                          onKeyDown={handlePriceKeyPress}
                            />
                            <Container className="error">
                                {
                                    wasPriceTouched && wasPriceOnBlur
                                    && (parseFloat(priceInput) < 0 || isNaN(parseFloat(priceInput)))
                                    && "Must be a float number and greater than 0."
                                }
                            </Container>
                        </Form.Group>
                        <ListGroup>
                            {tagList}
                        </ListGroup>
                        <br/>
                        <Form.Control type="text"
                                      name="tagInput"
                                      placeholder="Input tags"
                                      value={tagInput}
                                      onChange={handleInputChange}
                                      onKeyPress={handleTagKeyPress}
                        />
                        <Container className="error">
                            {tagErrorMessage &&
                            <Container className="error">{tagErrorMessage}</Container>}
                        </Container>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Cancel
                    </Button>
                    <Button disabled={!formValid} onClick={handleSubmit} variant="primary">
                        {certificate ? "Edit" : "Create"}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}