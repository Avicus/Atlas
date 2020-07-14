package net.avicus.atlas.core.module.checks.types;

import java.util.Optional;
import lombok.ToString;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.CheckResult;
import net.avicus.atlas.core.module.checks.variable.AttackerVariable;

/**
 * An attacker check is a wrapper check that contains player information about the cause of an
 * attack.
 */
@ToString
public class AttackerCheck implements Check {

  private final Check check;

  public AttackerCheck(Check check) {
    this.check = check;
  }

  @Override
  public CheckResult test(CheckContext context) {
    Optional<AttackerVariable> attacker = context.getFirst(AttackerVariable.class);

    if (!attacker.isPresent()) {
      return CheckResult.IGNORE;
    }

    return this.check.test(attacker.get());
  }
}