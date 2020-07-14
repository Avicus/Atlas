package net.avicus.atlas.core.module.stats;

import java.util.Optional;
import net.avicus.atlas.core.SpecificationVersionHistory;
import net.avicus.atlas.core.documentation.FeatureDocumentation;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.documentation.SpecInformation;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.util.xml.XmlElement;

public class StatsFactory implements ModuleFactory<StatsModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    return ModuleDocumentation.builder()
        .name("Facts/MVP Settings")
        .tagName("disable-stats")
        .description("This module is used to configure settings relating to match facts and MVP.")
        .category(ModuleDocumentation.ModuleCategory.CORE)
        .feature(FeatureDocumentation.builder()
            .name("Disable Stats")
            .specInformation(SpecInformation.builder()
                .added(SpecificationVersionHistory.UNTOUCHABLE_CHAT_CHANNELS).build())
            .description("If the module should be enabled during this match.")
            .description("Network stats have no relation to this value.")
            .build())
        .build();
  }

  @Override
  public Optional<StatsModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    if (root.hasChild("disable-stats")) {
      return Optional.empty();
    }

    return Optional.of(new StatsModule(match));
  }
}
