package net.avicus.atlas.core.channel.premium;

import net.avicus.atlas.core.channel.PrefixedChannel;
import org.bukkit.ChatColor;

public final class PremiumChannel extends PrefixedChannel {

  public PremiumChannel() {
    super("premium", "channel.premium", channelDescriptor("PREMIUM", ChatColor.GREEN));
  }
}
