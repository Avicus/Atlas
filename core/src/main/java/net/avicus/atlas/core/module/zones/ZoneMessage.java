package net.avicus.atlas.core.module.zones;

import lombok.ToString;
import net.avicus.atlas.core.module.locales.LocalizedXmlString;
import net.avicus.atlas.core.runtimeconfig.RuntimeConfigurable;
import net.avicus.atlas.core.runtimeconfig.fields.ConfigurableField;
import net.avicus.atlas.core.runtimeconfig.fields.EnumField;
import net.avicus.atlas.core.runtimeconfig.fields.LocalizedXmlField;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ToString
public class ZoneMessage implements RuntimeConfigurable {

  private LocalizedXmlString message;
  private ZoneMessageFormat format;

  public ZoneMessage(LocalizedXmlString message, ZoneMessageFormat format) {
    this.message = message;
    this.format = format;
  }

  public void send(Player player) {
    String text = this.message.translate(player);
    player.sendMessage(this.format.format(text));
  }

  @Override
  public ConfigurableField[] getFields() {
    return new ConfigurableField[]{
        new LocalizedXmlField("text", () -> this.message, (v) -> this.message = v),
        new EnumField<>("format", () -> this.format, (v) -> this.format = v, ZoneMessageFormat.class)
    };
  }

  @Override
  public String getDescription(CommandSender viewer) {
    return "Deny Message";
  }
}
