package net.avicus.atlas.core.module.objectives;

import java.util.Optional;
import net.avicus.atlas.core.module.groups.Competitor;

public interface StagnatedCompletionObjective extends GlobalObjective {

  Optional<Competitor> getHighestCompleter();


}
