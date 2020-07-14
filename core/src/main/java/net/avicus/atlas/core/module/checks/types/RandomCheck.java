package net.avicus.atlas.core.module.checks.types;

import java.util.Random;
import lombok.ToString;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.CheckResult;

/**
 * A random check randomly passes based on the supplied amount of randomness.
 */
@ToString
public class RandomCheck implements Check {

  private final static Random random = new Random();
  private final double value;

  public RandomCheck(double value) {
    this.value = value;
  }

  @Override
  public CheckResult test(CheckContext context) {
    return CheckResult.valueOf(random.nextDouble() <= this.value);
  }
}
