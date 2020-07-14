package net.avicus.atlas.core.match.registry;

import lombok.Getter;

public class RegisteredObject<T> implements RegisterableObject<T> {

  @Getter
  private final String id;
  @Getter
  private final T object;

  public RegisteredObject(String id, T object) {
    this.id = id;
    this.object = object;
  }
}
