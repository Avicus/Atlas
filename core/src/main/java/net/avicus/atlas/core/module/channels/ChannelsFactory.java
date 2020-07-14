package net.avicus.atlas.core.module.channels;

import java.util.Optional;
import net.avicus.atlas.core.SpecificationVersionHistory;
import net.avicus.atlas.core.documentation.FeatureDocumentation;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.documentation.SpecInformation;
import net.avicus.atlas.core.documentation.attributes.GenericAttribute;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.util.xml.XmlElement;
import net.avicus.atlas.core.util.xml.XmlException;

/**
 * Factory for the {@link ChannelsModule}
 */
public class ChannelsFactory implements ModuleFactory<ChannelsModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    return ModuleDocumentation.builder()
        .name("Chat Channels")
        .tagName("channels")
        .category(ModuleDocumentation.ModuleCategory.COMPONENTS)
        .specInformation(SpecInformation.builder()
            .deprecated(SpecificationVersionHistory.REPAIR_CHECK_DEF_OFF)
            .removed(SpecificationVersionHistory.UNTOUCHABLE_CHAT_CHANNELS)
            .build()
        )
        .feature(FeatureDocumentation.builder()
            .name("Main Configuration")
            .attribute("team-chat",
                new GenericAttribute(Boolean.class, false, "If team chat is enabled."), true)
            .attribute("global-chat",
                new GenericAttribute(Boolean.class, false, "If global chat is enabled."), true)
            .build()
        )
        .build();
  }

  @Override
  public Optional<ChannelsModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    XmlElement element = root.getChild("channels").orElse(null);

    boolean allowTeamChat = true;
    boolean allowGlobalChat = true;

    if (element != null) {
      if (!match.getMap().getSpecification()
          .greaterEqual(SpecificationVersionHistory.UNTOUCHABLE_CHAT_CHANNELS)) {
        allowTeamChat = element.getAttribute("team-chat").asRequiredBoolean();
        allowGlobalChat = element.getAttribute("global-chat").asRequiredBoolean();
      } else {
        match.warnDeprecation("Channels are no longer configurable",
            SpecificationVersionHistory.UNTOUCHABLE_CHAT_CHANNELS);
      }
    }

    if (!allowTeamChat && !allowGlobalChat) {
      throw new XmlException(element, "Team chat and global chat cannot be both disabled");
    }

    return Optional.of(new ChannelsModule(match, allowTeamChat, allowGlobalChat));
  }
}
