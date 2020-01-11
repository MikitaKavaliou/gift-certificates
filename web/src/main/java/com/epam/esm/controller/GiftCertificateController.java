package com.epam.esm.controller;

import com.epam.esm.builder.ServiceResponseBuilder;
import com.epam.esm.entity.CertificateWithTags;
import com.epam.esm.entity.ServiceResponse;
import com.epam.esm.service.GiftCertificateService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
   * @param certificateWithTags the certificate with tags
   * @param response            the response
   * @param request             the request
   * @return the service response
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping()
  public @ResponseBody
  ServiceResponse<String> createCertificate(@RequestBody CertificateWithTags certificateWithTags,
      HttpServletResponse response, HttpServletRequest request) {
    long certificateId = certificateService.create(certificateWithTags);
    response.setHeader("Location", request.getRequestURL().toString() + "/" + certificateId);
    return new ServiceResponseBuilder<String>()
        .status(HttpStatus.CREATED.toString())
        .build();
  }

  /**
   * Update certificate with tags returns service response.
   *
   * @param id                  the id of updating resource
   * @param certificateWithTags the certificate with tags
   * @param response            the response
   * @param request             the request
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{id}")
  public @ResponseBody
  ServiceResponse<String> updateCertificate(@PathVariable Long id,
      @RequestBody CertificateWithTags certificateWithTags,
      HttpServletResponse response, HttpServletRequest request) {
    certificateWithTags.setId(id);
    certificateService.update(certificateWithTags);
    response.setHeader("Location", request.getRequestURL().toString());
    return new ServiceResponseBuilder<String>()
        .status(HttpStatus.OK.toString())
        .build();
  }

  /**
   * Find by id service returns response.
   *
   * @param id the id of requested resource
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public @ResponseBody
  ServiceResponse<CertificateWithTags> findCertificateById(@PathVariable Long id) {
    CertificateWithTags certificate = certificateService.findById(id);
    return new ServiceResponseBuilder<CertificateWithTags>()
        .status(HttpStatus.OK.toString())
        .data(certificate)
        .build();
  }

  /**
   * Find all service returns response.
   *
   * @param parameters the parameters
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public @ResponseBody
  ServiceResponse<List<CertificateWithTags>> findAllCertificates(@RequestParam Map<String, String> parameters) {
    List<CertificateWithTags> certificateTags = certificateService.findCertificatesWithTags(parameters);
    return new ServiceResponseBuilder<List<CertificateWithTags>>()
        .status(HttpStatus.OK.toString())
        .data(certificateTags)
        .build();
  }

  /**
   * Delete certificate by id returns service response.
   *
   * @param id the id of deleting resource
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public @ResponseBody
  ServiceResponse<String> deleteCertificate(@PathVariable Long id) {
    certificateService.delete(id);
    return new ServiceResponseBuilder<String>()
        .status(HttpStatus.OK.toString())
        .build();
  }
}