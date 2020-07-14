package net.avicus.atlas.core.channel.staff;

import net.avicus.atlas.core.channel.PrefixedChannel;
import net.md_5.bungee.api.chat.BaseComponent;

public final class StaffChannel extends PrefixedChannel {

  public StaffChannel(String id, String permission, BaseComponent descriptor) {
    super(id, permission, descriptor);
  }
}
