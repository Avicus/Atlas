package net.avicus.atlas.core.module.checks.types;

import java.util.Optional;
import lombok.ToString;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.CheckResult;
import net.avicus.atlas.core.module.checks.variable.SpawnReasonVariable;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * A spawn check checks the reason an entity spawned.
 */
@ToString
public class SpawnCheck implements Check {

  private final SpawnReason reason;

  public SpawnCheck(SpawnReason reason) {
    this.reason = reason;
  }

  @Override
  public CheckResult test(CheckContext context) {
    Optional<SpawnReasonVariable> var = context.getFirst(SpawnReasonVariable.class);
    if (var.isPresent()) {
      return CheckResult.valueOf(this.reason == var.get().getReason());
    }
    return CheckResult.IGNORE;
  }
}
