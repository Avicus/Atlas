package net.avicus.atlas.core.module.objectives;

import net.avicus.atlas.core.documentation.FeatureDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public interface ObjectiveFactory<T extends Objective> {

  T build(Match match, MatchFactory factory, XmlElement element);

  FeatureDocumentation getDocumentation();
}
