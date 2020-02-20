package com.epam.esm.util;

import java.util.Map;
import org.apache.ibatis.session.RowBounds;

public class PaginationUtil {

  private static final String PAGE_NUMBER_REQUEST_PARAMETER = "page";
  private static final String RECORDS_PER_PAGE_REQUEST_PARAMETER = "perPage";

  private PaginationUtil() {

  }

  public static RowBounds createRowBounds(Map<String, String> parameters) {
    int pageNumber;
    int perPage;
    if (parameters.containsKey(PAGE_NUMBER_REQUEST_PARAMETER)
        && parameters.containsKey(RECORDS_PER_PAGE_REQUEST_PARAMETER)
        && (pageNumber = getNumericValue(parameters.get(PAGE_NUMBER_REQUEST_PARAMETER))) > 0
        && (perPage = getNumericValue(parameters.get(RECORDS_PER_PAGE_REQUEST_PARAMETER))) > 0) {
      return new RowBounds(perPage * (pageNumber - 1), perPage);
    } else {
      return new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
    }
  }

  private static int getNumericValue(String parameter) {
    try {
      return Math.max(Integer.parseInt(parameter), 0);
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}