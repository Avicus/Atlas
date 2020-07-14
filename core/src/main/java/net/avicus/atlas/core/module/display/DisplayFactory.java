package net.avicus.atlas.core.module.display;

import java.util.Optional;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.module.ModuleFactorySort;
import net.avicus.atlas.core.util.xml.XmlElement;

@ModuleFactorySort(ModuleFactorySort.Order.LAST)
public class DisplayFactory implements ModuleFactory<DisplayModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    // TODO: This should be a component.
    return null;
  }

  @Override
  public Optional<DisplayModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    return Optional.of(new DisplayModule(match));
  }
}
