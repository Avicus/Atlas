package net.avicus.atlas.core.documentation.attributes;

public interface Attribute {

  String getName();

  boolean isRequired();

  String[] getDescription();

  default String[] getValues() {
    return new String[]{};
  }

  default String getLink() {
    return "";
  }
}
