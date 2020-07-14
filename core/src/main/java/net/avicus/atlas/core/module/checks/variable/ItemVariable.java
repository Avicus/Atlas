package net.avicus.atlas.core.module.checks.variable;

import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.checks.Variable;
import net.avicus.atlas.core.util.ScopableItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * The item variable contains information about the type of item that the check is being performed
 * against. This variable includes all of an item's attributes.
 */
@ToString
public class ItemVariable implements Variable {

  @Getter
  private final ScopableItemStack itemStack;

  public ItemVariable(Match match, ItemStack itemStack) {
    this.itemStack = new ScopableItemStack(match, itemStack);
  }
}
