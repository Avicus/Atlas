package net.avicus.atlas.core.component;

import com.sk89q.bukkit.util.CommandsManagerRegistration;

/**
 * A module with commands.
 */
public interface CommandComponent extends Component {

  /**
   * Register commands.
   *
   * @param registrar the command registrar
   */
  void registerCommands(CommandsManagerRegistration registrar);
}
