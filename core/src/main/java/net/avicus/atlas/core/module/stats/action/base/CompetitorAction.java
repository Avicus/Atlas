package net.avicus.atlas.core.module.stats.action.base;

import net.avicus.atlas.core.module.groups.Competitor;

public interface CompetitorAction extends Action {

  Competitor getActor();
}
