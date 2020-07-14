package net.avicus.atlas.core.module.objectives.lcs;

import java.util.Collection;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.groups.GroupsModule;
import net.avicus.atlas.core.module.locales.LocalizedXmlString;
import net.avicus.atlas.core.module.objectives.Objective;

@ToString(exclude = "match")
public class LastCompetitorStanding implements Objective {

  private final Match match;

  public LastCompetitorStanding(Match match) {
    this.match = match;
  }

  public Collection<? extends Competitor> currentCompetitors() {
    return this.match.getRequiredModule(GroupsModule.class).getCompetitors();
  }

  @Override
  public void initialize() {

  }

  @Override
  public LocalizedXmlString getName() {
    return new LocalizedXmlString("Last Competitor Standing");
  }

  @Override
  public boolean canComplete(Competitor competitor) {
    return true;
  }

  @Override
  public boolean isCompleted(Competitor competitor) {
    Collection competitors = currentCompetitors();
    return competitors.size() == 1 && competitors.iterator().next().equals(competitor);
  }

  @Override
  public double getCompletion(Competitor competitor) {
    return 0;
  }

  @Override
  public boolean isIncremental() {
    return false;
  }
}
