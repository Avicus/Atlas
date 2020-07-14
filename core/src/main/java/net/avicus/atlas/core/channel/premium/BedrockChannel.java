package net.avicus.atlas.core.channel.premium;

import net.avicus.atlas.core.channel.PrefixedChannel;
import net.avicus.atlas.core.text.Components;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class BedrockChannel extends PrefixedChannel {

  public BedrockChannel() {
    super("bedrock", "channel.bedrock", channelDescriptor("BEDROCK", ChatColor.DARK_GRAY));
  }

  @Override
  public boolean send(CommandSender source, BaseComponent... components) {
    TextComponent pre = new TextComponent("");
    pre.addExtra(source.getName());
    pre.addExtra(Components.simple(": ", ChatColor.GRAY));
    pre.addExtra(Components.simple(components, ChatColor.WHITE));
    return super.send(source, pre);
  }
}
