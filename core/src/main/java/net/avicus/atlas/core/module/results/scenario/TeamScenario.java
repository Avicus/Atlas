package net.avicus.atlas.core.module.results.scenario;

import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.groups.GroupsModule;
import net.avicus.atlas.core.module.groups.teams.Team;
import net.avicus.atlas.core.module.states.StatesModule;

@ToString
public class TeamScenario extends EndScenario {

  @Getter
  private final Team team;

  public TeamScenario(Match match, Check check, int places, Team team) {
    super(match, check, places);
    this.team = team;
  }

  @Override
  public void execute(Match match, GroupsModule groups) {
    match.getRequiredModule(StatesModule.class).next();

    for (Competitor competitor : groups.getCompetitors(getTeam())) {
      handleWin(match, competitor);
    }
  }
}
