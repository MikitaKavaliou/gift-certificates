package com.epam.esm.controller;

import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Purchase;
import com.epam.esm.security.SecurityUserDetails;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.util.UrlProvider;
import com.epam.esm.validation.PurchaseValidator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

  private final PurchaseService purchaseService;

  @Autowired
  public PurchaseController(PurchaseService purchaseService) {
    this.purchaseService = purchaseService;
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping()
  public ResponseEntity<List<PurchaseWithCertificateDto>> findAllPurchasesByUserId(HttpServletRequest request) {
    SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return new ResponseEntity<>(purchaseService.findPurchasesByUserId(userDetails.getId(),
        UrlProvider.getUrlForCertificateFromPurchasesUrl(request.getRequestURL().toString())), HttpStatus.OK);
  }

  @Secured({"ROLE_ADMIN"})
  @GetMapping("/{userId}")
  public ResponseEntity<List<PurchaseWithCertificateDto>> findAllPurchasesByUserId(@PathVariable Long userId,
      HttpServletRequest request) {
    List<PurchaseWithCertificateDto> purchases = purchaseService.findPurchasesByUserId(userId,
        UrlProvider.getUrlForCertificateFromPurchasesByUserIdUrl(request.getRequestURL().toString()));
    return ResponseEntity.ok(purchases);
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PurchaseWithCertificateDto> createPurchase(@RequestBody Purchase purchase,
      HttpServletRequest request) {
    if (!PurchaseValidator.isValidPurchaseForCreation(purchase)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    purchase.setUserId(userDetails.getId());
    return new ResponseEntity<>(purchaseService.create(purchase,
        UrlProvider.getUrlForCertificateFromPurchasesUrl(request.getRequestURL().toString())), HttpStatus.OK);
  }
}