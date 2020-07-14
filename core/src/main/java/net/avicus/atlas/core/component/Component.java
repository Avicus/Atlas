package net.avicus.atlas.core.component;

/**
 * A module.
 */
public interface Component {

  /**
   * Invoked when enabling the module.
   */
  default void enable() {
  }

  /**
   * Invoked when disabling the module.
   */
  default void disable() {
  }
}
