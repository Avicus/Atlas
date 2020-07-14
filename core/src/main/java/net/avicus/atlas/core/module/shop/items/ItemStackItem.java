package net.avicus.atlas.core.module.shop.items;

import java.util.List;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.locales.LocalizedXmlString;
import net.avicus.atlas.core.module.shop.ShopItem;
import net.avicus.atlas.core.util.ScopableItemStack;
import org.bukkit.entity.Player;

/**
 * Represents a shop item that when purchased, rewards an {@link org.bukkit.inventory.ItemStack}.
 */
public class ItemStackItem extends ShopItem {

  /**
   * The stack to reward.
   */
  private final ScopableItemStack stack;

  /**
   * Constructor
   *
   * @param price The price of the item.
   * @param requiredLevel The required level needed to purchase the item.
   * @param name The name of the item.
   * @param description List of description lines for the item. This will never be null, but can be
   * empty.
   * @param purchaseCheck The check that should be ran before this item can be purchased.
   * @param stack The stack to reward.
   */
  public ItemStackItem(int price,
      LocalizedXmlString name,
      List<LocalizedXmlString> description,
      Check purchaseCheck, ScopableItemStack stack) {
    super(price, name, description, stack, purchaseCheck);
    this.stack = stack;
  }

  @Override
  public void give(Player player) {
    player.getInventory().addItem(this.stack.getItemStack(player));
  }
}
