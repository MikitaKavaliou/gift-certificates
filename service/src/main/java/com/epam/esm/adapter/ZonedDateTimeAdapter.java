package com.epam.esm.adapter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  @Override
  public ZonedDateTime unmarshal(String dateTime) {
    return ZonedDateTime.parse(dateTime, dateTimeFormatter);
  }

  @Override
  public String marshal(ZonedDateTime dateTime) {
    return dateTime.format(dateTimeFormatter);
  }
}