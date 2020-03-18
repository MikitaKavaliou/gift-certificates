package com.epam.esm.util;

import java.util.Map;
import org.apache.ibatis.session.RowBounds;

public class PaginationUtil {

  private static final String PAGE_NUMBER_PARAMETER = "page";
  private static final String RECORDS_PER_PAGE_PARAMETER = "perPage";
  private static final Integer DEFAULT_PAGE_NUMBER = 1;
  private static final Integer MIN_PAGE_NUMBER = 1;
  private static final Integer DEFAULT_PER_PAGE_RECORDS = 10;
  private static final Integer MIN_PER_PAGE_RECORDS = 1;
  private static final Integer MAX_PER_PAGE_RECORDS = 50;


  private PaginationUtil() {

  }

  public static RowBounds createRowBounds(Map<String, String> parameters) {
    try {
      int pageNumber = Integer
          .parseInt(parameters.getOrDefault(PAGE_NUMBER_PARAMETER, DEFAULT_PAGE_NUMBER.toString()));
      int perPage = Integer
          .parseInt(parameters.getOrDefault(RECORDS_PER_PAGE_PARAMETER, DEFAULT_PER_PAGE_RECORDS.toString()));
      return perPage >= MIN_PER_PAGE_RECORDS && perPage <= MAX_PER_PAGE_RECORDS && pageNumber >= MIN_PAGE_NUMBER ?
          new RowBounds(perPage * (pageNumber - 1), perPage) : getDefaultRowBounds();
    } catch (NumberFormatException e) {
      return getDefaultRowBounds();
    }
  }

  private static RowBounds getDefaultRowBounds() {
    return new RowBounds(DEFAULT_PER_PAGE_RECORDS * (DEFAULT_PAGE_NUMBER - 1), DEFAULT_PER_PAGE_RECORDS);
  }

  public static Integer calculatePagesCount(int recordsCount, int pageSize) {
    return (int) Math.ceil((double) recordsCount / pageSize);
  }
}