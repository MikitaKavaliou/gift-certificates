package com.epam.esm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlProvider {

  private static final String PURCHASES_REGEX = "purchases";
  private static final String CERTIFICATES_ID_CONTEXT_PATH = "certificates/";
  private static final String PURCHASES_BY_USER_ID_REGEX = "(purchases[/])\\d*";

  private UrlProvider() {

  }

  public static String getUrlForCertificateFromPurchasesUrl(String purchaseUrl) {
    return purchaseUrl.replace(PURCHASES_REGEX, CERTIFICATES_ID_CONTEXT_PATH);
  }

  public static String getUrlForCertificateFromPurchasesByUserIdUrl(String purchaseUrl) {
    Pattern pattern = Pattern.compile(PURCHASES_BY_USER_ID_REGEX);
    Matcher matcher = pattern.matcher(purchaseUrl);
    return matcher.replaceAll(CERTIFICATES_ID_CONTEXT_PATH);
  }
}