package com.epam.esm.validation;

import com.epam.esm.model.Tag;

public class TagValidator {

  private static final int MAX_TAG_NAME_LENGTH = 50;

  private TagValidator() {

  }

  public static boolean isValidTag(Tag tag) {
    String tagName = tag.getName();
    return tagName != null && !tagName.isEmpty() && tagName.length() <= MAX_TAG_NAME_LENGTH;
  }
}