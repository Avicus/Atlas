package net.avicus.atlas.core.module.damage;

import com.google.common.collect.Lists;
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
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.modifiers.AllCheck;
import net.avicus.atlas.core.util.xml.XmlElement;

public class DamageFactory implements ModuleFactory<DamageModule> {

  @Override
  public ModuleDocumentation getDocumentation() {
    return ModuleDocumentation.builder()
        .name("Damage Component")
        .tagName("damage")
        .description(
            "The damage module can be used to disable specific types of damage based on checks.")
        .category(ModuleDocumentation.ModuleCategory.MISC)
        .feature(FeatureDocumentation.builder()
            .name("Damage")
            .description(
                "These tags contain checks that are ran before damage occurs during the match.")
            .attribute("disable",
                Attributes.check(true, "before damage is applied to a player or entity"))
            .build())
        .build();
  }

  @Override
  public Optional<DamageModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    List<XmlElement> elements = root.getChildren("damage");

    AllCheck check = new AllCheck(Lists.newArrayList());

    if (elements.isEmpty()) {
      return Optional.empty();
    }

    elements.forEach(element -> {
      Check disable = FactoryUtils.resolveRequiredCheckChild(match, element.getAttribute("disable"),
          element.getChild("disable"));
      check.getChildren().add(disable);
    });

    return Optional.of(new DamageModule(match, check));
  }

}
