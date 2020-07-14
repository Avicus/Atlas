package net.avicus.atlas.core.module.invsee;

import java.util.Optional;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public class InvSeeFactory implements ModuleFactory<InvSeeModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    // TODO: This should be a component.
    return null;
  }

  @Override
  public Optional<InvSeeModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    // Always enabled
    return Optional.of(new InvSeeModule(match));
  }

}
