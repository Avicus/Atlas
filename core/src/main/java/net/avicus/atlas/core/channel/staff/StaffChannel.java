package net.avicus.atlas.core.channel.staff;

import net.avicus.atlas.core.channel.PrefixedChannel;
import net.avicus.atlas.core.text.Components;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class StaffChannel extends PrefixedChannel {

  public StaffChannel(String id, String permission, BaseComponent descriptor) {
    super(id, permission, descriptor);
  }
}
