package net.avicus.atlas.core.module.compass;

import java.util.Optional;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.util.Messages;
import org.bukkit.entity.Player;

public class DefaultCompassResolver implements CompassResolver {

  @Override
  public Optional<CompassView> resolve(Match match, Player player) {
    CompassView result = new CompassView(player.getLocation(), Messages.UI_COMPASS.with());
    return Optional.of(result);
  }
}
