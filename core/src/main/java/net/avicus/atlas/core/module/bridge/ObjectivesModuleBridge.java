package net.avicus.atlas.core.module.bridge;

import java.util.Optional;
import javax.annotation.Nullable;
import net.avicus.atlas.core.module.checks.CheckResult;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.groups.Group;
import net.avicus.atlas.core.module.groups.teams.Team;
import net.avicus.atlas.core.module.objectives.Objective;
import net.avicus.atlas.core.module.objectives.ObjectivesModule;
import net.avicus.compendium.number.NumberAction;
import org.bukkit.entity.Player;

public abstract class ObjectivesModuleBridge implements
    net.avicus.atlas.core.module.ModuleBridge<ObjectivesModule> {

  public boolean broadcastCompletion(Objective objective, Group group, Optional<Player> cause) {
    return false;
  }

  public void score(Competitor competitor, int reward, NumberAction action,
      @Nullable Player actor) {
  }

  public CheckResult performCaptureCheck(Objective objective, Team team) {
    return CheckResult.IGNORE;
  }

  public CheckResult performCaptureCheck(Objective objective) {
    return CheckResult.IGNORE;
  }
}
