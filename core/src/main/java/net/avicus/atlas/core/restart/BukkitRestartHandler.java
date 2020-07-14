package net.avicus.atlas.core.restart;

import org.bukkit.Bukkit;

public class BukkitRestartHandler implements RestartHandler {

  @Override
  public void queue() {
    if (Bukkit.getOnlinePlayers().isEmpty()) {
      Bukkit.shutdown();
    }
  }
}
