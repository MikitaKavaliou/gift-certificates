package com.epam.esm.controller;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.dto.PurchaseGiftCertificateIdDto;
import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Purchase;
import com.epam.esm.security.SecurityUserDetails;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.util.UrlProvider;
import com.epam.esm.validation.PurchaseValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Purchase controller.
 */
@RestController
@RequestMapping(value = "/purchases", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseController {

  private final PurchaseService purchaseService;

  /**
   * Instantiates a new Purchase controller.
   *
   * @param purchaseService the purchase service
   */
  @Autowired
  public PurchaseController(PurchaseService purchaseService) {
    this.purchaseService = purchaseService;
  }

  /**
   * Find all purchases by user id response entity.
   *
   * @param userDetails the user details
   * @param parameters  the parameters
   * @param request     the request
   * @return the response entity
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping()
  public ResponseEntity<EntityListDto<PurchaseWithCertificateDto>> findAllPurchasesByUserId(
      @AuthenticationPrincipal SecurityUserDetails userDetails,
      @RequestParam Map<String, String> parameters, HttpServletRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(purchaseService.findPurchasesByUserId(userDetails.getId(),
            UrlProvider.getUrlForCertificateFromPurchasesUrl(request.getRequestURL().toString()), parameters));
  }

  /**
   * Find any user purchases response entity.
   *
   * @param userId     the user id
   * @param parameters the parameters
   * @param request    the request
   * @return the response entity
   */
  @Secured({"ROLE_ADMIN"})
  @GetMapping(params = "userId")
  public ResponseEntity<EntityListDto<PurchaseWithCertificateDto>> findAnyUserPurchases(@RequestParam Long userId,
      @RequestParam Map<String, String> parameters, HttpServletRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(purchaseService.findPurchasesByUserId(userId,
            UrlProvider.getUrlForCertificateFromPurchasesByUserIdUrl(request.getRequestURL().toString()), parameters));
  }

  /**
   * Create purchase response entity.
   *
   * @param userDetails               the user details
   * @param purchaseGiftCertificateId the purchase gift certificate id
   * @param request                   the request
   * @return the response entity
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PurchaseWithCertificateDto> createPurchase(
      @AuthenticationPrincipal SecurityUserDetails userDetails,
      @RequestBody PurchaseGiftCertificateIdDto purchaseGiftCertificateId,
      HttpServletRequest request) {
    Purchase purchase = new Purchase(userDetails.getId(), purchaseGiftCertificateId.getGiftCertificateId());
    if (!PurchaseValidator.isValidPurchaseForCreation(purchase)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(purchaseService
            .create(purchase, UrlProvider.getUrlForCertificateFromPurchasesUrl(request.getRequestURL().toString())));
  }
}