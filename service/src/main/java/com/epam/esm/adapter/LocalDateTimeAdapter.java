package com.epam.esm.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  @Override
  public LocalDateTime unmarshal(String dateTime) {
    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }

  @Override
  public String marshal(LocalDateTime dateTime) {
    return dateTime.format(dateTimeFormatter);
  }
}