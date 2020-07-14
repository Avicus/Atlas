package net.avicus.atlas.core.match.registry;

public interface RegisterableObject<T> {

  String getId();

  T getObject();
}
