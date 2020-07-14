package net.avicus.atlas.core.module.elimination.executor;

import java.util.Optional;
import java.util.UUID;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.FactoryUtils;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.elimination.EliminationModule;
import net.avicus.atlas.core.module.executors.Executor;
import net.avicus.atlas.core.util.xml.XmlElement;
import net.avicus.atlas.core.util.xml.XmlException;

/**
 * An executor used to toggle elimination on/off for a match.
 */
public class ToggleEliminationExecutor extends Executor {

  private final EliminationModule module;
  private final Optional<Boolean> setTo;

  public ToggleEliminationExecutor(String id, Check check, EliminationModule module,
      Optional<Boolean> setTo) {
    super(id, check);
    this.module = module;
    this.setTo = setTo;
  }

  public static Executor parse(Match match, XmlElement element) throws XmlException {
    Check check = FactoryUtils
        .resolveRequiredCheckChild(match, element.getAttribute("check"), element.getChild("check"));
    String id = element.getAttribute("id").asString().orElse(UUID.randomUUID().toString());
    EliminationModule module = match.getRequiredModule(EliminationModule.class);
    Optional<Boolean> setTo = element.getAttribute("to").asBoolean();
    return new ToggleEliminationExecutor(id, check, module, setTo);
  }

  @Override
  public void execute(CheckContext context) {
    boolean enabled = this.setTo.orElse(!this.module.isEnabled());
    this.module.setEnabled(enabled);
  }
}
