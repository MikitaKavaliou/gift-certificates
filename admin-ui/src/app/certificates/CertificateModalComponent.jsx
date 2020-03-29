import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import React, {useEffect, useState} from "react";
import Form from "react-bootstrap/Form";
import {Formik} from "formik";
import * as Yup from "yup"
import Octicon, {X} from "@primer/octicons-react";
import ListGroup from "react-bootstrap/ListGroup";
import Container from "react-bootstrap/Container";
import InputGroup from 'react-bootstrap/InputGroup'
import {useLocation} from "react-router-dom";

export function CertificateModal({certificate, token, modalState, closeModal, createCertificate, updateCertificate}) {
  const [tags, setTags] = useState([]);
  const location = useLocation();

  useEffect(() => {
    if (certificate) {
      certificate.tags ? setTags(certificate.tags) : setTags([]);
    }
  }, [certificate]);

  const schema = Yup.object({
    name: Yup.string()
    .min(5, "Min length is 5")
    .max(30, "Max length is 30")
    .required("Name field is required"),
    description: Yup.string()
    .min(12, "Min length is 12")
    .max(1000, "Max length is 1000")
    .required("Description field is required"),
    duration: Yup.number()
    .integer("Must be an Integer")
    .min(0, "Min value is 0")
    .max(10000, "Max value is 10000")
    .required("Duration field is required"),
    price: Yup.number()
    .min(0.00, "Min value is 0")
    .max(10000.00, "Max value is 10000")
    .required("Price field is required"),
  });

  const initialValues = {
    name: certificate ? certificate.name : "",
    description: certificate ? certificate.description : "",
    duration: certificate ? "" + certificate.duration : "",
    price: certificate ? "" + certificate.price : "",
    tags: ""
  };

  const handleTagRemove = i => {
    setTags(tags.filter((tag, index) => i !== index));
  };

  const handleAddTag = (value, setFieldValue, setFieldError) => {
    let isDuplicate = false;
    for (let tag of tags) {
      if (tag.name === value) {
        isDuplicate = true;
      }
    }
    if (isDuplicate) {
      setFieldError("tags", "Such tag already added", false);
    } else if (value.length < 3) {
      setFieldError("tags", "Min tag length is 3", false);
    } else {
      setTags([...tags, {name: value}]);
      setFieldValue("tags", "");
    }
  };

  const tagList = (() => {
    let items = [];
    tags.map((tag, index) =>
        items.push(
            <ListGroup.Item className="input-tags" key={index}>{tag.name}&nbsp;
              <Container className="icon-container"
                         onClick={() => handleTagRemove(index)}>
                <Octicon icon={X}/>
              </Container>
            </ListGroup.Item>)
    );
    return items;
  })();

  const handleKeyDown = (e, value) => {
    if (e.target.name === "tags" && value.length >= 15 && (e.key !== "Backspace"
        && e.key !== "Delete"
        && e.key !== "ArrowLeft" && e.key !== "ArrowRight")) {
      e.preventDefault();
    } else if (e.target.name === "price" && value.indexOf(".") > -1
        && value.split(".")[1].length >= 2 && e.target.selectionStart
        > value.indexOf(".")
        && (e.key !== "Backspace" && e.key !== "Delete"
            && e.key !== "ArrowLeft" && e.key !== "ArrowRight")) {
      e.preventDefault();
    }
  };

  const isCertificateChanged = values => {
    return certificate ? values.name !== certificate.name || values.description
        !== certificate.description ||
        parseInt(values.duration) !== certificate.duration || parseFloat(
            values.price) !== certificate.price ||
        JSON.stringify(tags) !== JSON.stringify(certificate.tags) : true;
  };

  const handleSubmit = values => {
    certificate ? update(values) : create(values);
    closeModal();
  };

  const create = (values) => {
    const newCertificate = {
      name: values.name,
      description: values.description,
      duration: parseInt(values.duration),
      price: parseFloat(values.price),
      tags: tags
    };
    createCertificate(token, newCertificate, location.search)
  };

  const update = (values) => {
    const tagsForAdding = getTagsForAdding();
    const tagsForDeletion = getTagsForDeletion();
    const duration = parseInt(values.duration);
    const price = parseFloat(values.price);
    const updateObject = {
      name: values.name !== certificate.name ? values.name : null,
      description: values.description !== certificate.description
          ? values.description : null,
      duration: duration !== certificate.duration ? duration : null,
      price: price !== certificate.price ? price : null,
      tagsForAdding: tagsForAdding,
      tagsForDeletion: tagsForDeletion,

    };
    updateCertificate(token, certificate.id, updateObject, location.search);
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
      <Modal centered show={modalState} onHide={closeModal}>
        <Formik
            validationSchema={schema}
            onSubmit={handleSubmit}
            initialValues={initialValues}
        >
          {({
            handleSubmit,
            handleChange,
            handleBlur,
            values,
            touched,
            errors,
            setFieldValue,
            setFieldError
          }) => (
              <Form onSubmit={handleSubmit}>
                <Modal.Header closeButton>
                  <Modal.Title>{certificate ? "Edit certificate with id = "
                      + certificate.id : "Add new certificate"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <Form.Group controlId="nameForm">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type="text"
                                  name="name"
                                  placeholder="Certificate name"
                                  value={values.name}
                                  onChange={handleChange}
                                  onBlur={handleBlur}
                                  isInvalid={touched.name && !!errors.name}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.name}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group controlId="descriptionForm">
                    <Form.Label>Description</Form.Label>
                    <Form.Control as="textarea"
                                  rows="3"
                                  className="text-area"
                                  name="description"
                                  placeholder="Certificate description"
                                  value={values.description}
                                  onChange={handleChange}
                                  onBlur={handleBlur}
                                  isInvalid={touched.description
                                  && !!errors.description}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.description}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group controlId="durationForm">
                    <Form.Label>Duration</Form.Label>
                    <Form.Control type="text"
                                  name="duration"
                                  placeholder="Certificate duration"
                                  value={values.duration}
                                  onChange={handleChange}
                                  onBlur={handleBlur}
                                  isInvalid={touched.duration
                                  && !!errors.duration}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.duration}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group controlId="priceForm">
                    <Form.Label>Price</Form.Label>
                    <Form.Control type="text"
                                  name="price"
                                  placeholder="Certificate price"
                                  value={values.price}
                                  onChange={handleChange}
                                  onKeyDown={e => handleKeyDown(e,
                                      values.price)}
                                  onPaste={e => e.preventDefault()}
                                  onBlur={handleBlur}
                                  isInvalid={touched.price && !!errors.price}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.price}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <ListGroup>
                    {tagList}
                  </ListGroup>
                  <br/>
                  <InputGroup>
                    <Form.Control type="text"
                                  name="tags"
                                  placeholder="Input tags"
                                  value={values.tags}
                                  onChange={handleChange}
                                  onKeyDown={e => handleKeyDown(e, values.tags)}
                                  onPaste={e => e.preventDefault()}
                                  onBlur={handleBlur}
                                  isInvalid={errors.tags}
                    />
                    <InputGroup.Append>
                      <Button onClick={() => handleAddTag(values.tags,
                          setFieldValue, setFieldError)}
                              variant="primary">Add</Button>
                    </InputGroup.Append>
                    <Form.Control.Feedback type="invalid">
                      {errors.tags}
                    </Form.Control.Feedback>
                  </InputGroup>
                </Modal.Body>
                <Modal.Footer>
                  <Button variant="secondary" onClick={closeModal}>
                    Cancel
                  </Button>
                  <Button disabled={!isCertificateChanged(values)} type="submit"
                          variant="primary">
                    {certificate ? "Edit" : "Create"}
                  </Button>
                </Modal.Footer>
              </Form>
          )}
        </Formik>
      </Modal>
  )
}