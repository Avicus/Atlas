package net.avicus.atlas.core.util;

import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.objectives.Objective;
import org.bukkit.entity.Player;

public abstract class ObjectiveRenderer {

  public abstract String getDisplay(Match match, Competitor competitor, Player viewer,
      Objective objective, boolean showName);
}
