package net.avicus.atlas.core.component;

import com.google.common.collect.Maps;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * A module manager.
 */
public class ComponentManager {

  /**
   * A map of registered modules.
   */
  protected final Map<Class<? extends Component>, Component> modules = Maps.newHashMap();
  /**
   * The plugin manager.
   */
  protected final PluginManager pluginManager;
  /**
   * The plugin owning this module manager.
   */
  protected final Plugin plugin;
  /**
   * The command registrar.
   */
  protected final CommandsManagerRegistration commandRegistrar;

  public ComponentManager(PluginManager pluginManager, Plugin plugin,
      CommandsManagerRegistration commandRegistrar) {
    this.pluginManager = pluginManager;
    this.plugin = plugin;
    this.commandRegistrar = commandRegistrar;
  }

  /**
   * Register a module if a condition is met.
   *
   * @param clazz the module class
   * @param condition the condition
   * @param <M> the module class type
   * @see #register(Class)
   */
  public <M extends Component> void register(Class<M> clazz, boolean condition) {
    if (condition) {
      this.register(clazz);
    }
  }

  /**
   * Register a module.
   *
   * @param clazz the module class
   * @param <M> the module class type
   */
  public <M extends Component> void register(Class<M> clazz) {
    try {
      this.modules.put(clazz, clazz.newInstance());
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets a module by its class.
   *
   * @param clazz the module class
   * @param <M> the module class type
   * @return the module factory
   * @throws IllegalStateException if the specified module class has not been registered
   */
  @SuppressWarnings("unchecked")
  public <M extends Component> M get(Class<M> clazz) {
    @Nullable final M module = (M) this.modules.get(clazz);
    if (module != null) {
      return module;
    }

    throw new IllegalStateException("Could not find registered module for " + clazz.getName());
  }

  /**
   * Check if a module is loaded for a class.
   *
   * @param clazz the module class
   * @param <M> the module class type
   * @return the module factory
   */
  @SuppressWarnings("unchecked")
  public boolean hasModule(Class<? extends Component> clazz) {
    return this.modules.containsKey(clazz);
  }

  /**
   * Enable all modules.
   */
  public void enable() {
    for (Component component : this.modules.values()) {
      component.enable();

      if (component instanceof Listener) {
        this.pluginManager.registerEvents((Listener) component, this.plugin);
      }

      if (component instanceof CommandComponent) {
        ((CommandComponent) component).registerCommands(this.commandRegistrar);
      }
    }
  }

  /**
   * Disable all modules.
   */
  public void disable() {
    for (Component component : this.modules.values()) {
      component.disable();

      if (component instanceof Listener) {
        HandlerList.unregisterAll((Listener) component);
      }
    }
  }
}
