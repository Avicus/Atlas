package net.avicus.atlas.core.module.checks.variable;

import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.Variable;

/**
 * The attacker variable is a wrapper variable that contains player and location information about
 * the cause of an attack.
 */
@ToString(callSuper = true)
public class AttackerVariable extends CheckContext implements Variable {

  public AttackerVariable(Match match) {
    super(match);
  }
}
