package net.avicus.atlas.core.module.zones;

import lombok.ToString;
import net.avicus.atlas.core.module.locales.LocalizedXmlString;
import org.bukkit.entity.Player;

@ToString
public class ZoneMessage {

  private final LocalizedXmlString message;
  private final ZoneMessageFormat format;

  public ZoneMessage(LocalizedXmlString message, ZoneMessageFormat format) {
    this.message = message;
    this.format = format;
  }

  public void send(Player player) {
    String text = this.message.translate(player);
    player.sendMessage(this.format.format(text));
  }
}
