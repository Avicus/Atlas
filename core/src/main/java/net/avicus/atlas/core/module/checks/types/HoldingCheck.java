package net.avicus.atlas.core.module.checks.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.ToString;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.CheckResult;
import net.avicus.atlas.core.module.checks.variable.PlayerVariable;
import net.avicus.atlas.core.util.ScopableItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A holding check checks the type of item a player is holding in their hand.
 */
@ToString
public class HoldingCheck implements Check {

  private final ScopableItemStack itemStack;

  public HoldingCheck(ScopableItemStack itemStack) {
    this.itemStack = itemStack;
  }

  @Override
  public CheckResult test(CheckContext context) {
    Optional<PlayerVariable> optional = context.getFirst(PlayerVariable.class);

    if (!optional.isPresent()) {
      return CheckResult.IGNORE;
    }

    Player player = optional.get().getPlayer();

    List<ItemStack> contents = new ArrayList<>();
    contents.add(player.getInventory().getItemInHand());

    for (ItemStack test : contents) {
      boolean matches = this.itemStack.equals(player, test);
      if (matches) {
        return CheckResult.ALLOW;
      }
    }

    return CheckResult.DENY;
  }
}
