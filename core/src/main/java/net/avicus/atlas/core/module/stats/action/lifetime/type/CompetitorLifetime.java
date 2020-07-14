package net.avicus.atlas.core.module.stats.action.lifetime.type;

import java.time.Instant;
import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.stats.action.base.CompetitorAction;

@ToString
public class CompetitorLifetime extends ActionLifetime<CompetitorAction> {

  @Getter
  private final Competitor competitor;

  public CompetitorLifetime(Instant start, Competitor competitor) {
    super(start);
    this.competitor = competitor;
  }
}
