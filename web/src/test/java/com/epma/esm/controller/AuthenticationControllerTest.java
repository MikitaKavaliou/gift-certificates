package com.epma.esm.controller;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.epam.esm.config.RepositoryConfig;
import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.config.WebConfig;
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
public class AuthenticationControllerTest {

  private static final String LOGIN_ENDPOINT = "/login";
  private static final String SIGNUP_ENDPOINT = "/signup";
  private static final String VALIDATE_ADMIN_TOKEN_ENDPOINT = "/token?admin";
  private static final String TOKEN_OBJECT_SCHEMA_NAME =
      "validation/token/token-object-validation-schema.json";
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
  private String userToken;
  private String adminToken;

  @Before
  public void initializeRestAssuredMockMvcWebApplicationContext() {
    MockMvc mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(authenticationFilter).build();
    RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    RestAssuredMockMvc.mockMvc(mockMvc);
    MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(authenticationFilter);
    userToken = tokenService.createToken(new User(1L, "username", "password", Role.USER)).getToken();
    adminToken = tokenService.createToken(new User(2L, "username2", "password", Role.ADMIN)).getToken();
    jsonSchemaFactory = JsonSchemaFactory
        .newBuilder().setValidationConfiguration(ValidationConfiguration
            .newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
            .freeze())
        .freeze();
  }

  @Test
  public void loginTestSuccessfullyLoggedInReturnsToken() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username", "password", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(LOGIN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TOKEN_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }


  @Test
  public void loginTestBadCredentialsReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username", "passwordsdfsdf", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(LOGIN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void loginTestIncorrectDataReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, null, null, Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(LOGIN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void loginTestForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username", "password", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .post(LOGIN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void signUpTestSuccessfullySignedUpReturnsToken() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username324", "password", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(SIGNUP_ENDPOINT)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TOKEN_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void signUpTestUsernameExistsReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username", "password", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(SIGNUP_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void signUpTestForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, "username", "password", Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .post(SIGNUP_ENDPOINT)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void signUpTestIncorrectDataReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User(1L, null, null, Role.USER);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(user))
        .header(AUTHORIZATION_HEADER_NAME, "adminToken")
        .when()
        .post(SIGNUP_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void validateAdminTokenCorrectTokenTest() {
    given()
        .contentType(ContentType.JSON)
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(VALIDATE_ADMIN_TOKEN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void validateAdminTokenUserTokenForbiddenTest() {
    given()
        .contentType(ContentType.JSON)
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .post(VALIDATE_ADMIN_TOKEN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void validateAdminTokenGuestUnauthorizedTest() {
    given()
        .contentType(ContentType.JSON)
        .header(AUTHORIZATION_HEADER_NAME, "incorrect token")
        .when()
        .post(VALIDATE_ADMIN_TOKEN_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }
}