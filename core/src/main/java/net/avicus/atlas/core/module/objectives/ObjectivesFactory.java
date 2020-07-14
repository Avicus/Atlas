package net.avicus.atlas.core.module.objectives;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.avicus.atlas.core.documentation.ModuleDocumentation;
import net.avicus.atlas.core.documentation.attributes.Attribute;
import net.avicus.atlas.core.documentation.attributes.EnumAttribute;
import net.avicus.atlas.core.documentation.attributes.GenericAttribute;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.match.registry.RegisteredObject;
import net.avicus.atlas.core.module.ModuleBuildException;
import net.avicus.atlas.core.module.ModuleFactory;
import net.avicus.atlas.core.module.ModuleFactorySort;
import net.avicus.atlas.core.module.objectives.entity.EntityFactory;
import net.avicus.atlas.core.module.objectives.lcs.LastCompetitorStandingFactory;
import net.avicus.atlas.core.module.objectives.lts.LastTeamStandingFactory;
import net.avicus.atlas.core.module.objectives.score.ScoreObjectiveFactory;
import net.avicus.atlas.core.util.distance.DistanceCalculationMetric;
import net.avicus.atlas.core.util.xml.XmlElement;
import org.apache.commons.lang3.tuple.Pair;

@ModuleFactorySort(ModuleFactorySort.Order.LATE)
public class ObjectivesFactory implements ModuleFactory<ObjectivesModule> {

  public static final Map<String, ObjectiveFactory> FACTORY_MAP = Maps.newHashMap();

  static {
    FACTORY_MAP.put("last-team-standing", new LastTeamStandingFactory());
    FACTORY_MAP.put("last-competitor-standing", new LastCompetitorStandingFactory());
    FACTORY_MAP.put("entity", new EntityFactory());
    FACTORY_MAP.put("score", new ScoreObjectiveFactory());
  }

  public static LinkedHashMap<String, Pair<Attribute, Object>> proximity(String prefix, String when,
      boolean required, DistanceCalculationMetric.Type defType, boolean defHoriz) {
    LinkedHashMap<String, Pair<Attribute, Object>> res = Maps.newLinkedHashMap();

    res.put(prefix + "-dist-metric", Pair.of(
        new EnumAttribute(DistanceCalculationMetric.Type.class, required,
            "Calculation metric used to calculate distance from objective " + when + "."),
        defType));
    res.put(prefix + "-dist-horiz", Pair.of(new GenericAttribute(Boolean.class, required,
        "If only horizontal distance should be used for calculation."), defHoriz));

    return res;
  }

  @Override
  public ModuleDocumentation getDocumentation() {
    ModuleDocumentation.ModuleDocumentationBuilder builder = ModuleDocumentation.builder()
        .name("Objectives")
        .tagName("objectives")
        .category(ModuleDocumentation.ModuleCategory.CORE)
        .description(
            "Objectives are the main way that a match winner is decided. They must be completed by a team to win, and sometimes have an owner who must protect the objective from other competitors.");

    List<Class> doc = Lists.newArrayList();
    FACTORY_MAP.values().forEach(f -> {
      if (doc.contains(f.getClass())) {
        return;
      }
      builder.feature(f.getDocumentation());
      doc.add(f.getClass());
    });

    return builder.build();
  }

  @Override
  public Optional<ObjectivesModule> build(Match match, MatchFactory factory, XmlElement root)
      throws ModuleBuildException {
    List<XmlElement> elements = root.getChildren("objectives");

    if (elements.isEmpty()) {
      return Optional.empty();
    }

    List<Objective> objectives = new ArrayList<>();

    elements.forEach(element -> {
      for (XmlElement child : element.getDescendants()) {
        String name = child.getName();

        ObjectiveFactory objectiveFactory = FACTORY_MAP.get(name);

        if (objectiveFactory == null) {
          continue;
        }

        Objective objective = objectiveFactory.build(match, factory, child);
        if (child.hasAttribute("id")) {
          RegisteredObject registered = new RegisteredObject<>(
              child.getAttribute("id").asRequiredString(), objective);
          match.getRegistry().add(registered);
        }
        objectives.add(objective);
      }
    });

    return Optional.of(new ObjectivesModule(match, objectives));
  }

  public void addObjective(Objective objective, Match match) {
    if (match.hasModule(ObjectivesModule.class)) {
      match.getRequiredModule(ObjectivesModule.class).getObjectives().add(objective);
    } else {
      match.addModule(new ObjectivesModule(match, Lists.newArrayList(objective)));
    }
  }
}
