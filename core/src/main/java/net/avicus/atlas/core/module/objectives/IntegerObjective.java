package net.avicus.atlas.core.module.objectives;

import javax.annotation.Nullable;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.compendium.number.NumberAction;
import org.bukkit.entity.Player;

public interface IntegerObjective extends Objective {

  int getPoints(Competitor competitor);

  void modify(Competitor competitor, int amount, NumberAction action,
      @Nullable Player actor);
}
