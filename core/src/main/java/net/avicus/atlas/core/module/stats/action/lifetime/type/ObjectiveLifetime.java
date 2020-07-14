package net.avicus.atlas.core.module.stats.action.lifetime.type;

import java.time.Instant;
import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.module.objectives.Objective;
import net.avicus.atlas.core.module.stats.action.objective.ObjectiveAction;

@ToString
public class ObjectiveLifetime extends ActionLifetime<ObjectiveAction> {

  @Getter
  private final Objective objective;

  public ObjectiveLifetime(Instant start, Objective objective) {
    super(start);
    this.objective = objective;
  }
}
