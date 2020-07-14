package net.avicus.atlas.core.module.kills;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.avicus.atlas.core.documentation.FeatureDocumentation;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.documentation.attributes.Attributes;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.module.FactoryUtils;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.module.ModuleFactorySort;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.loadouts.Loadout;
import net.avicus.atlas.core.util.xml.XmlElement;

@ModuleFactorySort(ModuleFactorySort.Order.LATE) // After loadouts
public class KillsFactory implements ModuleFactory<KillsModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    return ModuleDocumentation.builder()
        .category(ModuleDocumentation.ModuleCategory.MISC)
        .name("Kills/Deaths")
        .tagName("kills")
        .description(
            "This module can be used to apply checks to kills and deaths and to give out kill rewards.")
        .feature(FeatureDocumentation.builder()
            .name("Kill/Death Checks")
            .tagName("kill-check")
            .tagName("death-check")
            .description("Checks that should run before players can get a kills/deaths.")
            .attribute("kill-check", Attributes.check(true, "before a player can get a kill"))
            .attribute("death-check", Attributes.check(true, "before a player can die"))
            .build())
        .feature(FeatureDocumentation.builder()
            .name("Kill Rewards")
            .tagName("rewards")
            .description("This is used to reward loadouts to players upon successful kills.")
            .attribute("loadout", Attributes
                .idOf(true, "loadout", "Loadout that should be given to the player upon a kill."))
            .attribute("check",
                Attributes.check(false, "before the loadout is rewarded to the killer"))
            .build())
        .build();
  }

  @Override
  public Optional<KillsModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    List<XmlElement> elements = root.getChildren("kills");

    Optional<Check> killCheck = Optional.empty();
    Optional<Check> deathCheck = Optional.empty();

    List<KillReward> rewards = new ArrayList<>();

    if (!elements.isEmpty()) {
      for (XmlElement element : elements) {
        killCheck = FactoryUtils.resolveCheckChild(match, element.getAttribute("kill-check"),
            element.getChild("kill-check"));
        deathCheck = FactoryUtils.resolveCheckChild(match, element.getAttribute("death-check"),
            element.getChild("death-check"));

        XmlElement el = element.getChild("rewards").orElse(null);
        if (el != null) {
          for (XmlElement child : el.getChildren("reward")) {
            Loadout loadout = FactoryUtils
                .resolveRequiredLoadout(match, child.getAttribute("loadout"),
                    child.getChild("loadout"));
            Optional<Check> check = FactoryUtils
                .resolveCheckChild(match, child.getAttribute("check"), child.getChild("check"));
            rewards.add(new KillReward(loadout, check));
          }
        }
      }
    }

    return Optional.of(new KillsModule(match, rewards, killCheck, deathCheck));
  }
}
