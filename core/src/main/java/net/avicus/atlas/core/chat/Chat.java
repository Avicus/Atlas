package net.avicus.atlas.core.chat;

import com.sk89q.minecraft.util.commands.ChatColor;
import net.avicus.atlas.core.AtlasConfig;
import net.avicus.atlas.core.network.user.Users;
import net.avicus.atlas.core.util.Events;
import net.avicus.compendium.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Modifies chat display with ranks.
 */
public class Chat implements Listener {

  public static void init() {
    Events.register(new Chat());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
    if (AtlasConfig.Chat.isStripColor()) {
      String message = Strings.removeColors(event.getMessage());
      event.setMessage(message);
    } else {
      String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
      event.setMessage(message);
    }

    String name = Users.getDisplay(event.getPlayer());
    String message = event.getMessage();

    String full = String.format(event.getFormat(), name, message);

    for (Player player : event.getRecipients()) {
      player.sendMessage(full);
    }
    Bukkit.getConsoleSender().sendMessage(full);

    event.getRecipients().clear();
    event.setCancelled(true);
  }
}
