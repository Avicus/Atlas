package net.avicus.atlas.core.module.compass;

import java.util.Optional;
import net.avicus.atlas.core.match.Match;
import org.bukkit.entity.Player;

public interface CompassResolver {

  Optional<CompassView> resolve(Match match, Player player);
}
