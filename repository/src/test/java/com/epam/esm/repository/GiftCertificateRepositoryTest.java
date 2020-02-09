package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.repository.specification.impl.certificate.CertificateIdSpecification;
import com.epam.esm.repository.specification.impl.certificate.CertificatesCriteriaSpecification;
import com.epam.esm.repository.specification.impl.tag.TagCertificateIdSpecification;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.h2.tools.RunScript;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class GiftCertificateRepositoryTest {

  private List<GiftCertificate> giftCertificates;
  private List<Tag> tags;
  @Autowired
  private GiftCertificateRepository giftCertificateRepository;
  @Autowired
  private DataSource dataSource;
  @Autowired
  private TagRepository tagRepository;

  @Before
  public void setUp() throws SQLException, FileNotFoundException {
    File file = ResourceUtils.getFile("classpath:database.sql");
    RunScript.execute(dataSource.getConnection(), new FileReader((file)));
    setUpTags();
    setUpGiftCertificates();
  }

  private void setUpGiftCertificates() {
    giftCertificates = new ArrayList<>();
    giftCertificates.add(new GiftCertificate(9L, "pizza_certificate",
        "50% discount for 5 pizzas", BigDecimal.valueOf(10.50).setScale(2, RoundingMode.CEILING),
        Timestamp.valueOf("2020-01-18 18:53:21").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        Timestamp.valueOf("2020-01-18 18:53:21").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        4));
    giftCertificates.add(new GiftCertificate(10L, "certificate_for_rent_car",
        "car-sharing-5$-free", BigDecimal.valueOf(3.00).setScale(2, RoundingMode.CEILING),
        Timestamp.valueOf("2020-01-18 18:54:26").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        Timestamp.valueOf("2020-01-18 18:57:18").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        4));
    giftCertificates.add(new GiftCertificate(11L, "ninja_sushi_certificate",
        "every second sushi is free", BigDecimal.valueOf(3.00).setScale(2, RoundingMode.CEILING),
        Timestamp.valueOf("2020-01-18 18:58:24").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        Timestamp.valueOf("2020-01-18 18:58:24").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        2));
    giftCertificates.add(new GiftCertificate(12L, "healty_food_certificate",
        "every salad 25% discount", BigDecimal.valueOf(2.00).setScale(2, RoundingMode.CEILING),
        Timestamp.valueOf("2020-01-18 18:59:14").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        Timestamp.valueOf("2020-01-18 18:59:14").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        1));
    giftCertificates.add(new GiftCertificate(13L, "kfc_delivery_ceritificate",
        "free delivery over 10$", BigDecimal.valueOf(2.50).setScale(2, RoundingMode.CEILING),
        Timestamp.valueOf("2020-01-18 19:00:52").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        Timestamp.valueOf("2020-01-18 19:00:52").toLocalDateTime().atZone(ZoneOffset.systemDefault()),
        10));
  }

  private void setUpTags() {
    tags = new ArrayList<>();
    tags.add(new Tag(6L, "pizza"));
    tags.add(new Tag(7L, "discount"));
    tags.add(new Tag(8L, "certificate"));
    tags.add(new Tag(9L, "car"));
    tags.add(new Tag(10L, "rent"));
    tags.add(new Tag(11L, "for_rent"));
    tags.add(new Tag(12L, "sushi"));
    tags.add(new Tag(13L, "food"));
    tags.add(new Tag(14L, "delivery"));
    tags.add(new Tag(15L, "salad"));
    tags.add(new Tag(16L, "kfc"));
  }

  @Test
  public void certificateIdSpecificationTest() {
    List<GiftCertificate> expected = Collections.singletonList(giftCertificates.get(3));
    List<GiftCertificate> actual = giftCertificateRepository
        .query(new CertificateIdSpecification(12L));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createCertificateTest() {
    long expectedId = 14;
    GiftCertificate giftCertificate = giftCertificates.get(0);
    giftCertificateRepository.create(giftCertificate, new ArrayList<>());
    List<GiftCertificate> certificates = giftCertificateRepository.query(new CertificateIdSpecification(expectedId));
    long actualId = certificates.get(0).getId();
    Assert.assertEquals(expectedId, actualId);
  }

  @Test
  public void deleteCertificateTest() {
    Long id = giftCertificates.get(0).getId();
    giftCertificateRepository.delete(id);
    List<GiftCertificate> actualCertificates = giftCertificateRepository.query(new CertificateIdSpecification(id));
    Assert.assertTrue(actualCertificates.isEmpty());
  }

  @Test
  public void createCertificateWithTags() {
    GiftCertificate giftCertificate = new GiftCertificate();
    List<Long> tagIdList = Collections.singletonList(tags.get(0).getId());
    long id = giftCertificateRepository.create(giftCertificate, tagIdList);
    List<Tag> tagList = tagRepository.query(new TagCertificateIdSpecification(id));
    long expectedTagId = 6;
    long actualTagId = tagList.get(0).getId();
    Assert.assertEquals(expectedTagId, actualTagId);
  }

  @Test
  public void updateCertificateTest() {
    String expectedNewCertificateName = "new_name";
    GiftCertificate giftCertificate = giftCertificates.get(2);
    long certificateId = giftCertificate.getId();
    giftCertificate.setName(expectedNewCertificateName);
    giftCertificate.setDescription(null);
    giftCertificateRepository.update(giftCertificate, new ArrayList<>(), new ArrayList<>());
    List<GiftCertificate> certificates = giftCertificateRepository.query(new CertificateIdSpecification(certificateId));
    String actualCertificateName = certificates.get(0).getName();
    Assert.assertEquals(expectedNewCertificateName, actualCertificateName);
  }

  @Test
  public void updateCertificateWithTags() {
    GiftCertificate giftCertificate = new GiftCertificate();
    giftCertificate.setId(giftCertificates.get(1).getId());
    List<Long> tagIdList = Collections.singletonList(tags.get(0).getId());
    giftCertificateRepository.update(giftCertificate, tagIdList, new ArrayList<>());
    List<Tag> tagList = tagRepository.query(new TagCertificateIdSpecification(giftCertificate.getId()));
    int tagListExpectedSize = 4;
    int tagListActualSize = tagList.size();
    Assert.assertEquals(tagListExpectedSize, tagListActualSize);
  }

  @Test
  public void certificatesCriteriaSpecificationNoCriteriaTest() {
    Map<String, String> stringMap = new HashMap<>();
    List<GiftCertificate> certificates =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(giftCertificates, certificates);
  }

  @Test
  public void certificatesCriteriaSpecificationWithSearchValue() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("searchValue", "rent");
    List<GiftCertificate> expectedList = Collections.singletonList(giftCertificates.get(1));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void certificatesCriteriaSpecificationWithMultipleSearchValues() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("searchValue", "rent other");
    List<GiftCertificate> expectedList = Collections.singletonList(giftCertificates.get(1));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void certificatesCriteriaSpecificationWithSingleTag() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("tag", "rent");
    List<GiftCertificate> expectedList = Collections.singletonList(giftCertificates.get(1));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void certificatesCriteriaSpecificationWithMultipleTags() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("tag", "rent car");
    List<GiftCertificate> expectedList = Collections.singletonList(giftCertificates.get(1));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void certificatesCriteriaSpecificationWithSortField() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("sortField", "name");
    List<GiftCertificate> expectedList = Arrays.asList(giftCertificates.get(1), giftCertificates.get(3),
        giftCertificates.get(4), giftCertificates.get(2), giftCertificates.get(0));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void certificatesCriteriaSpecificationWithSortFieldAndSortType() {
    Map<String, String> stringMap = new HashMap<>();
    stringMap.put("sortField", "name");
    stringMap.put("sortType", "ASC");
    List<GiftCertificate> expectedList = Arrays.asList(giftCertificates.get(1), giftCertificates.get(3),
        giftCertificates.get(4), giftCertificates.get(2), giftCertificates.get(0));
    List<GiftCertificate> actualList =
        giftCertificateRepository.query(new CertificatesCriteriaSpecification(stringMap));
    Assert.assertEquals(expectedList, actualList);
  }

  @Configuration
  @PropertySource("classpath:database.properties")
  public static class GiftCertificateServiceTestContextConfiguration {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(driver);
      dataSource.setUrl(url);
      dataSource.setUsername(user);
      dataSource.setPassword(password);
      return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
      return new JdbcTemplate(dataSource);
    }

    @Bean
    public GiftCertificateRepository giftCertificateRepository(JdbcTemplate jdbcTemplate) {
      return new GiftCertificateRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public TagRepository tagRepository(JdbcTemplate jdbcTemplate) {
      return new TagRepositoryImpl(jdbcTemplate);
    }
  }
}