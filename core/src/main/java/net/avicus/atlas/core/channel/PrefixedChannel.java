package net.avicus.atlas.core.channel;

import net.avicus.atlas.core.channel.staff.StaffChannels;
import net.avicus.atlas.core.text.Components;
import net.avicus.compendium.settings.PlayerSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PrefixedChannel extends SimpleChannel {

  private final BaseComponent prefix;

  protected PrefixedChannel(String id, String permission, BaseComponent prefix) {
    super(id, permission);
    this.prefix = prefix;
  }

  public static BaseComponent channelDescriptor(String name, ChatColor color) {
    TextComponent component = new TextComponent(name);
    component.setBold(true);
    component.setColor(color.asBungee());
    return component;
  }

  @Override
  public boolean canRead(final CommandSender viewer) {
    return
        !(viewer instanceof Player && !PlayerSettings.get((Player) viewer, StaffChannels.SETTING))
            && super.canRead(viewer);
  }

  @Override
  public void format(CommandSender source, BaseComponent component) {
    component.addExtra(Components.simple("[", ChatColor.GRAY));
    component.addExtra(prefix);
    component.addExtra(Components.simple("]", ChatColor.GRAY));
    component.addExtra(" ");

    super.format(source, component);
  }
}
