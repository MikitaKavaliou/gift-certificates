package com.epam.esm.validation;

import com.epam.esm.model.Tag;
import org.apache.commons.lang3.StringUtils;

public class TagValidator {

  private static final int MAX_TAG_NAME_LENGTH = 50;

  private TagValidator() {

  }

  public static boolean isValidTag(Tag tag) {
    return StringUtils.isNotBlank(tag.getName()) && tag.getName().length() <= MAX_TAG_NAME_LENGTH;
  }
}