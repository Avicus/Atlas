package net.avicus.atlas.core.module.objectives.lcs;

import net.avicus.atlas.core.documentation.FeatureDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.objectives.ObjectiveFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public class LastCompetitorStandingFactory implements ObjectiveFactory<LastCompetitorStanding> {

  @Override
  public FeatureDocumentation getDocumentation() {
    return FeatureDocumentation.builder()
        .name("Last Competitor Standing")
        .tagName("last-competitor-standing")
        .description(
            "The last competitor standing objective is used to mark the winner of the match as the competitor (team for normal, player for FFA) with any players on it after all others are empty.")
        .build();
  }

  @Override
  public LastCompetitorStanding build(Match match, MatchFactory factory, XmlElement element) {
    return new LastCompetitorStanding(match);
  }
}
