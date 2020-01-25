package com.epam.esm.repository.specification.impl.certificate;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Certificates criteria specification. Creates database query for finding certificates by tag names, by part
 * of name/description, sort by date/name DESC/ASC (all params are optional).
 */
public class CertificatesCriteriaSpecification implements Specification {

  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String CREATE_DATE = "create_date";
  private static final String UPDATE_DATE = "last_update_date";
  private static final String BASIC_QUERY = "SELECT DISTINCT G.gift_certificate_id, g.name, description, price, "
      + "create_date, "
      + "last_update_date, duration FROM gift_certificate G ";
  private static final String JOIN_TAG_TABLE_QUERY = "JOIN tag_gift_certificate TG ON G.gift_certificate_id=TG"
      + ".gift_certificate_id JOIN TAG T ON TG.tag_id=T.tag_id WHERE (T.name = ? ";
  private static final String CLOSING_BRACKET = ") ";
  private static final String AND = "AND ";
  private static final String WHERE = "WHERE ";
  private static final String TAG_NAME_CONDITION = "OR T.name = ? ";
  private static final String QUERY_BY_NAME = "G.name LIKE CONCAT( '%',?,'%') ";
  private static final String QUERY_BY_DESCRIPTION = "G.description LIKE CONCAT( '%',?,'%') ";
  private static final String ORDER_BY_NAME = "ORDER BY G.name ";
  private static final String ORDER_BY_CREATE_DATE = "ORDER BY G.create_date ";
  private static final String ORDER_BY_UPDATE_DATE = "ORDER BY G.last_update_date ";
  private static final String ORDER_TYPE_ASC = "ASC";
  private static final String ORDER_TYPE_DESC = "DESC";

  private List<String> tagNames;
  private String searchField;
  private String searchFieldValue;
  private String sortField;
  private String sortType;
  private Map<String, String> queryConditions = new HashMap<>();
  private Map<String, String> sortConditions = new HashMap<>();
  private Map<String, String> sortTypes = new HashMap<>();
  private List<String> parameters = new ArrayList<>();

  private CertificatesCriteriaSpecification() {
  }

  /**
   * Instantiates a new Certificates criteria specification.
   *
   * @param parameters the parameters
   */
  public CertificatesCriteriaSpecification(Map<String, String> parameters) {
    this.tagNames = parameters.containsKey("tag") ? Arrays.asList(parameters.get("tag").split(" ")) : null;
    this.searchField = parameters.containsKey("searchField") ? parameters.get("searchField").toLowerCase() : null;
    this.searchFieldValue =
        parameters.containsKey("searchFieldValue") ? parameters.get("searchFieldValue").toLowerCase() : null;
    this.sortField = parameters.containsKey("sortField") ? parameters.get("sortField").toLowerCase() : null;
    this.sortType = parameters.containsKey("sortType") ? parameters.get("sortType").toUpperCase() : null;
    initializeMaps();
  }

  private void initializeMaps() {
    queryConditions.put(NAME, QUERY_BY_NAME);
    queryConditions.put(DESCRIPTION, QUERY_BY_DESCRIPTION);
    sortConditions.put(NAME, ORDER_BY_NAME);
    sortConditions.put(CREATE_DATE, ORDER_BY_CREATE_DATE);
    sortConditions.put(UPDATE_DATE, ORDER_BY_UPDATE_DATE);
    sortTypes.put(ORDER_TYPE_ASC, ORDER_TYPE_ASC);
    sortTypes.put(ORDER_TYPE_DESC, ORDER_TYPE_DESC);
  }

  @Override
  public SqlRequest createSqlRequest() {
    StringBuilder sb = new StringBuilder(BASIC_QUERY);
    buildQuery(sb);
    return new SqlRequest(sb.toString(), parameters.toArray());
  }

  private void buildQuery(StringBuilder sb) {
    appendTagCondition(sb);
    appendSearchCondition(sb);
    appendSortCondition(sb);
  }

  private void appendTagCondition(StringBuilder sb) {
    if (tagNames != null) {
      sb.append(JOIN_TAG_TABLE_QUERY);
      parameters.add(tagNames.get(0));
      if (tagNames.size() > 1) {
        for (int i = 1; i < tagNames.size(); i++) {
          sb.append(TAG_NAME_CONDITION);
          parameters.add(tagNames.get(i));
        }
      }
      sb.append(CLOSING_BRACKET);
    }
  }

  private void appendSearchCondition(StringBuilder sb) {
    if (searchFieldValue != null && queryConditions.containsKey(searchField)) {
      if (tagNames == null) {
        sb.append(WHERE);
      } else {
        sb.append(AND);
      }
      sb.append(queryConditions.get(searchField));
      parameters.add(searchFieldValue);
    }
  }

  private void appendSortCondition(StringBuilder sb) {
    if (sortConditions.containsKey(sortField)) {
      sb.append(sortConditions.get(sortField));
      appendSortType(sb);
    }
  }

  private void appendSortType(StringBuilder sb) {
    if (sortTypes.containsKey(sortType)) {
      sb.append(sortTypes.get(sortType));
    }
  }
}