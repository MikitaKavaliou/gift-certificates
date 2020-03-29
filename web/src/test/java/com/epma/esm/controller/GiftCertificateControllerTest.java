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
import com.epam.esm.dto.GiftCertificatePriceDto;
import com.epam.esm.dto.GiftCertificateUpdateDto;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class GiftCertificateControllerTest {

  private static final String ALL_CERTIFICATES_ENDPOINT = "/api/certificates";
  private static final String CERTIFICATE_LIST_SCHEMA_NAME =
      "validation/certificate/certificate-list-validation-schema.json";
  private static final String CERTIFICATE_OBJECT_SCHEMA_NAME =
      "validation/certificate/certificate-object-validation-schema.json";
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
  private GiftCertificateMapper certificateMapper;
  @MockBean
  private TagMapper tagMapper;
  private JsonSchemaFactory jsonSchemaFactory;
  private GiftCertificateWithTagsDto giftCertificateWithTagsDto;
  private GiftCertificateUpdateDto giftCertificateUpdateDto;
  private String adminToken;
  private String userToken;
  private GiftCertificate giftCertificate;
  private Tag tag;


  @Before
  public void initializeRestAssuredMockMvcWebApplicationContext() {
    MockMvc mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(authenticationFilter).build();
    RestAssuredMockMvc.mockMvc(mockMvc);
    giftCertificate = new GiftCertificate(10L, "name", "description", BigDecimal.valueOf(3.5),
        LocalDateTime.now(), LocalDateTime.now(), 5);
    GiftCertificate giftCertificateForCreation = new GiftCertificate(10L, "name", "description",
        BigDecimal.valueOf(3.5),
        null, null, 5);
    tag = new Tag(1L, "for_rent");
    adminToken = tokenService.createToken(new User(8L, "username2", "password", Role.ADMIN)).getToken();
    userToken = tokenService.createToken(new User(1L, "username", "password", Role.USER)).getToken();
    giftCertificateWithTagsDto = new GiftCertificateWithTagsDto(giftCertificateForCreation,
        Collections.singletonList(tag));
    giftCertificateUpdateDto =
        new GiftCertificateUpdateDto("name", "description", BigDecimal.valueOf(5), 2,
            Collections.singletonList(tag), Collections.singletonList(tag));
    jsonSchemaFactory = JsonSchemaFactory
        .newBuilder().setValidationConfiguration(ValidationConfiguration
            .newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
            .freeze())
        .freeze();
  }

  @Test
  public void findAllCertificatesTest() {
    when(certificateMapper.selectByCriteria(any(), any(), any()))
        .thenReturn(Collections.singletonList(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    when(certificateMapper.getCountOfSuitableRecordsOfFindByCriteria(any(), any())).thenReturn(1);
    given().when().get(ALL_CERTIFICATES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat().body(matchesJsonSchemaInClasspath(CERTIFICATE_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findCertificateByIdTestReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .when().get(ALL_CERTIFICATES_ENDPOINT + "/47")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findCertificateByIdTestReturnsExceptionObject() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    given()
        .when().get(ALL_CERTIFICATES_ENDPOINT + "/40")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createCertificateCorrectDataReturnsCreatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateWithTagsDto))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_CERTIFICATES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createCertificateIncorrectDataReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    giftCertificateWithTagsDto.setDuration(-4);
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateWithTagsDto))
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .post(ALL_CERTIFICATES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createCertificateUnauthorizedReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateWithTagsDto))
        .when()
        .post(ALL_CERTIFICATES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void createCertificateForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateWithTagsDto))
        .when()
        .post(ALL_CERTIFICATES_ENDPOINT)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findUserCertificatesCorrectRequestReturnsCertificates() {
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(Collections.singletonList(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .get(ALL_CERTIFICATES_ENDPOINT + "?userCertificates")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findUserCertificatesUnauthorizedReturnsExceptionObject() {
    given()
        .when()
        .get(ALL_CERTIFICATES_ENDPOINT + "?userCertificates")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }


  @Test
  public void findAnyUserCertificatesCorrectRequestExceptionObject() {
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(Collections.singletonList(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .when()
        .get(ALL_CERTIFICATES_ENDPOINT + "?userId=8")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_LIST_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void findAnyUserCertificatesUnauthorizedReturnsExceptionObject() {
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(Collections.singletonList(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .when()
        .get(ALL_CERTIFICATES_ENDPOINT + "?userId=8")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updateCertificateCorrectDataReturnsUpdatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateUpdateDto))
        .when()
        .patch(ALL_CERTIFICATES_ENDPOINT + "/47")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updateCertificateIncorrectDataReturnsUpdatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    giftCertificateUpdateDto.setPrice(BigDecimal.valueOf(-3));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateUpdateDto))
        .when()
        .patch(ALL_CERTIFICATES_ENDPOINT + "/47")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updateCertificateUnauthorizedReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    given()
        .header(AUTHORIZATION_HEADER_NAME, "someToken")
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateUpdateDto))
        .when()
        .patch(ALL_CERTIFICATES_ENDPOINT + "/47")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updateCertificateForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificateUpdateDto))
        .when()
        .patch(ALL_CERTIFICATES_ENDPOINT + "/47")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updatePriceUnauthorizedReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(Collections.singletonList(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    GiftCertificatePriceDto giftCertificatePriceDto = new GiftCertificatePriceDto(BigDecimal.valueOf(5));
    given()
        .header(AUTHORIZATION_HEADER_NAME, "someToken")
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificatePriceDto))
        .when()
        .put(ALL_CERTIFICATES_ENDPOINT + "/47?price")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updatePriceForbiddenReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    GiftCertificatePriceDto giftCertificatePriceDto = new GiftCertificatePriceDto(BigDecimal.valueOf(5));
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificatePriceDto))
        .when()
        .put(ALL_CERTIFICATES_ENDPOINT + "/47?price")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updatePriceNegativePriceReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    GiftCertificatePriceDto giftCertificatePriceDto = new GiftCertificatePriceDto(BigDecimal.valueOf(-5));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificatePriceDto))
        .when()
        .put(ALL_CERTIFICATES_ENDPOINT + "/47?price")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updatePriceNullPriceReturnsExceptionObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    GiftCertificatePriceDto giftCertificatePriceDto = new GiftCertificatePriceDto();
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificatePriceDto))
        .when()
        .put(ALL_CERTIFICATES_ENDPOINT + "/47?price")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void updatePriceCorrectDataReturnsUpdatedObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(giftCertificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(Collections.singletonList(tag));
    GiftCertificatePriceDto giftCertificatePriceDto = new GiftCertificatePriceDto(BigDecimal.valueOf(5));
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(giftCertificatePriceDto))
        .when()
        .put(ALL_CERTIFICATES_ENDPOINT + "/47?price")
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(CERTIFICATE_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }



  @Test
  public void deleteCertificateUnauthorizedReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, "someToken")
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_CERTIFICATES_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void deleteCertificateForbiddenReturnsExceptionObject() {
    given()
        .header(AUTHORIZATION_HEADER_NAME, userToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_CERTIFICATES_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .assertThat()
        .body(matchesJsonSchemaInClasspath(EXCEPTION_OBJECT_SCHEMA_NAME).using(jsonSchemaFactory));
  }

  @Test
  public void deleteCertificateSuccessfulDeletion() {
    when(certificateMapper.delete(any())).thenReturn(1);
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_CERTIFICATES_ENDPOINT + "/9")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deleteCertificateNotFound() {
    when(certificateMapper.delete(any())).thenReturn(0);
    given()
        .header(AUTHORIZATION_HEADER_NAME, adminToken)
        .contentType(ContentType.JSON)
        .when()
        .delete(ALL_CERTIFICATES_ENDPOINT + "/20")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
}