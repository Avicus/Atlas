package net.avicus.atlas.core.module.vote;

import java.util.Optional;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public final class VoteModuleFactory implements ModuleFactory<VoteModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    // TODO: This should be a component.
    return null;
  }

  @Override
  public Optional<VoteModule> build(final Match match, final MatchFactory factory,
      final XmlElement root) throws ModuleBuildException {
    return Optional.of(new VoteModule(match));
  }
}
