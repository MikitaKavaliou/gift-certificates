package com.epma.esm.controller;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.epam.esm.config.RepositoryConfig;
import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.config.WebConfig;
import com.epam.esm.model.Purchase;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.security.AuthenticationFilter;
import com.epam.esm.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SecurityConfig.class, WebConfig.class, ServiceConfig.class,
    RepositoryConfig.class})
public class PurchaseControllerTest {

  private static final String ALL_PURCHASES_ENDPOINT = "/purchases";
  private static final String PURCHASE_LIST_SCHEMA_NAME =
      "validation/purchase/purchase-list-validation-schema.json";
  private static final String PURCHASE_OBJECT_SCHEMA_NAME =
      "validation/purchase/purchase-object-validation-schema.json";
  private static final String EXCEPTION_OBJECT_SCHEMA_NAME =
      "validation/exception/exception-object-validation-schema.json";
  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private AuthenticationFilter authenticationFilter;
  @Autowired
  private TokenService tokenService;
  private JsonSchemaFactory jsonSchemaFactory;
  private String adminToken;
  private String userToken;

  @Before
  public void initializeRestAssuredMockMvcWebApplicationContext() {
    MockMvc mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(authenticationFilter).build();
    RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    RestAssuredMockMvc.mockMvc(mockMvc);
    MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(authenticationFilter);
    adminToken = tokenService.createToken(new User(47L, "username2", "password", Role.ADMIN));
    userToken = tokenService.createToken(new User(1L, "username", "password", Role.USER));
    jsonSchemaFactory = JsonSchemaFactory
        .newBuilder().setValidationConfiguration(ValidationConfiguration
            .newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
            .freeze())
        .freeze();
  }

  @Test
  public void findAllPurchasesByUserIdTestReturnsPurchases() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .get(ALL_PURCHASES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat().body(matchesJsonSchemaInClasspath(PURCHASE_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAllPurchasesByUserIdTestUnauthorizedReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "userToken")
        .when()
        .get(ALL_PURCHASES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat().body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAnyUserPurchasesByUserIdTestReturnsPurchases() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .get(ALL_PURCHASES_ENDPOINT + "?userId=8")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat().body(matchesJsonSchemaInClasspath(PURCHASE_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAnyUserPurchasesByUserIdTestForbiddenReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .get(ALL_PURCHASES_ENDPOINT + "?userId=8")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat().body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAnyUserPurchasesByUserIdTestUnauthorizedReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "userToken")
        .when()
        .get(ALL_PURCHASES_ENDPOINT + "?userId=8")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat().body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createPurchaseCorrectDataReturnsCreatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Purchase purchase = new Purchase();
    purchase.setGiftCertificateId(13L);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(purchase))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_PURCHASES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(PURCHASE_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createPurchaseIncorrectDataReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Purchase purchase = new Purchase();
    purchase.setGiftCertificateId(-13L);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(purchase))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_PURCHASES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createPurchaseUnauthorizedReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Purchase purchase = new Purchase();
    purchase.setGiftCertificateId(-13L);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(purchase))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(ALL_PURCHASES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }
}