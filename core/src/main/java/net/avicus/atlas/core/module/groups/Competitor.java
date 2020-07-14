package net.avicus.atlas.core.module.groups;

import net.avicus.atlas.core.module.locales.LocalizedXmlString;
import net.avicus.atlas.core.util.color.TeamColor;
import net.avicus.atlas.core.util.distance.PlayerStore;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.text.Localizable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface Competitor extends PlayerStore {

  String getId();

  LocalizedXmlString getName();

  default Localizable getColoredName() {
    return getName().toText(TextStyle.ofColor(getChatColor()));
  }

  Group getGroup();

  boolean hasPlayer(Player player);

  TeamColor getTeamColor();

  default ChatColor getChatColor() {
    return getTeamColor().getChatColor();
  }

  default Color getFireworkColor() {
    return getTeamColor().getFireworkColor();
  }
}
