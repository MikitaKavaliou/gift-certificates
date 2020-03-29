package com.epma.esm.controller;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.epam.esm.config.RepositoryConfig;
import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.config.WebConfig;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.security.AuthenticationFilter;
import com.epam.esm.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SecurityConfig.class, WebConfig.class, ServiceConfig.class,
    RepositoryConfig.class})
public class TagControllerTest {

  private static final String ALL_TAGS_ENDPOINT = "/api/tags";
  private static final String TAG_LIST_SCHEMA_NAME =
      "validation/tag/tag-list-validation-schema.json";
  private static final String TAG_OBJECT_SCHEMA_NAME =
      "validation/tag/tag-object-validation-schema.json";
  private static final String EXCEPTION_OBJECT_SCHEMA_NAME =
      "validation/exception/exception-object-validation-schema.json";
  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private AuthenticationFilter authenticationFilter;
  @Autowired
  private TokenService tokenService;
  @MockBean
  private TagMapper tagMapper;
  private JsonSchemaFactory jsonSchemaFactory;
  private String adminToken;
  private String userToken;

  @Before
  public void initializeRestAssuredMockMvcWebApplicationContext() {
    MockMvc mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(authenticationFilter).build();
    RestAssuredMockMvc.mockMvc(mockMvc);
    adminToken = tokenService.createToken(new User(47L, "username2", "password", Role.ADMIN)).getToken();
    userToken = tokenService.createToken(new User(1L, "username", "password", Role.USER)).getToken();
    jsonSchemaFactory = JsonSchemaFactory
        .newBuilder().setValidationConfiguration(ValidationConfiguration
            .newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
            .freeze())
        .freeze();
  }

  @Test
  public void createTagCorrectDataReturnsCreatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Tag tag = new Tag(1L, "some new tag_name");
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(tag))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TAG_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createTagIncorrectDataReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Tag tag = new Tag(1L, null);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(tag))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createTagUnauthorizedReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Tag tag = new Tag(1L, null);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(tag))
        .header(AUTHORIZATION_HEADER_NAME, "someToken")
        .when()
        .post(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createTagForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Tag tag = new Tag(1L, null);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(tag))
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when()
        .post(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findTagByIdTestReturnsTag() {
    when(tagMapper.selectById(any())).thenReturn(Optional.of(new Tag(1L, "name")));
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when().get(ALL_TAGS_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TAG_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findTagByIdTestUnauthorizedReturnsTag() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "some toke")
        .when().get(ALL_TAGS_ENDPOINT + "/109")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAllTagsTestReturnsTags() {
    when(tagMapper.selectAll(any())).thenReturn(Collections.singletonList(new Tag(1L, "name")));
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when().get(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TAG_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAllTagsTestUnauthorizedReturnsTag() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "some toke")
        .when().get(ALL_TAGS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findTheMostPopularTagTestReturnsTag() {
    when(tagMapper.selectMostPopularTagOfHighestSpendingUser()).thenReturn(new Tag(1L, "name"));
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .when().get(ALL_TAGS_ENDPOINT + "?mostPopular")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(TAG_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findTheMostPopularTagTestUnauthorizedReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "userToken")
        .when().get(ALL_TAGS_ENDPOINT + "?mostPopular")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void deleteTagUnauthorizedReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "someToken")
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_TAGS_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void deleteTagForbiddenReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_TAGS_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void deleteTagSuccessfulDeletion() {
    when(tagMapper.deleteById(any())).thenReturn(1);
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_TAGS_ENDPOINT + "/8")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deleteTagNotFound() {
    when(tagMapper.deleteById(any())).thenReturn(0);
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_TAGS_ENDPOINT + "/4")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
}