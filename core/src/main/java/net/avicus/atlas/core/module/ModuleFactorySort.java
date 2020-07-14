package net.avicus.atlas.core.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleFactorySort {

  Order value();

  enum Order {
    FIRST,
    EARLY,
    NORMAL,
    LATE,
    LAST
  }
}
