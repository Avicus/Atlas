package net.avicus.atlas.core.module;

import org.bukkit.event.Listener;

public interface Module extends Listener {

  default void open() {

  }

  default void close() {

  }
}
