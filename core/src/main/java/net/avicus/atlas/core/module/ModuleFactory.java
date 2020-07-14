package net.avicus.atlas.core.module;

import java.util.Optional;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public interface ModuleFactory<M extends Module> {

  Optional<M> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException;

  ModuleDocumentation getDocumentation();
}
