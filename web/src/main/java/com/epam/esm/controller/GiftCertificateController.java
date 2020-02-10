package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificateWithTags;
import com.epam.esm.entity.GiftCertificatesList;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validation.GiftCertificateWithTagsValidator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Gift certificate controller. The class used for processing GiftCertificate-related requests.
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

  private GiftCertificateService certificateService;

  /**
   * Instantiates a new Gift certificate controller.
   *
   * @param certificateService the certificate service
   */
  @Autowired
  public GiftCertificateController(GiftCertificateService certificateService) {
    this.certificateService = certificateService;
  }

  /**
   * Create gift certificate with tags returns service response.
   *
   * @param giftCertificateWithTags the certificate with tags
   * @return the service response
   */
  @PostMapping()
  public ResponseEntity<GiftCertificateWithTags> createCertificate(
      @RequestBody GiftCertificateWithTags giftCertificateWithTags) {
    if (!GiftCertificateWithTagsValidator.isValidGiftCertificate(giftCertificateWithTags)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    long certificateId = certificateService.create(giftCertificateWithTags);
    GiftCertificateWithTags createdGiftCertificateWithTags = certificateService.findById(certificateId);
    return new ResponseEntity<>(createdGiftCertificateWithTags, HttpStatus.CREATED);
  }

  /**
   * Find by id service returns response.
   *
   * @param id the id of requested resource
   * @return the service response
   */
  @GetMapping("/{id}")
  public ResponseEntity<GiftCertificateWithTags> findCertificateById(@PathVariable Long id) {
    GiftCertificateWithTags giftCertificateWithTags = certificateService.findById(id);
    return new ResponseEntity<>(giftCertificateWithTags, HttpStatus.OK);
  }

  /**
   * Find all service returns response.
   *
   * @param parameters the parameters
   * @return the service response
   */
  @GetMapping()
  public ResponseEntity<GiftCertificatesList> findAllCertificates(@RequestParam Map<String, String> parameters) {
    List<GiftCertificateWithTags> certificatesWithTags = certificateService.findCertificatesWithTags(parameters);
    return new ResponseEntity<>(new GiftCertificatesList(certificatesWithTags), HttpStatus.OK);
  }

  /**
   * Update certificate with tags returns service response.
   *
   * @param id                      the id of updating resource
   * @param giftCertificateWithTags the certificate with tags
   * @return the service response
   */
  @PatchMapping("/{id}")
  public ResponseEntity<GiftCertificateWithTags> updateCertificate(@PathVariable Long id,
      @RequestBody GiftCertificateWithTags giftCertificateWithTags) {
    if (!GiftCertificateWithTagsValidator.isValidGiftCertificate(giftCertificateWithTags)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    giftCertificateWithTags.setId(id);
    long certificateId = certificateService.update(giftCertificateWithTags);
    GiftCertificateWithTags updatedGiftCertificateWithTags = certificateService.findById(certificateId);
    return new ResponseEntity<>(updatedGiftCertificateWithTags, HttpStatus.OK);
  }

  /**
   * Delete certificate by id returns service response.
   *
   * @param id the id of deleting resource
   * @return the service response
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
    int numberOfDeletedRows = certificateService.delete(id);
    return numberOfDeletedRows > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}