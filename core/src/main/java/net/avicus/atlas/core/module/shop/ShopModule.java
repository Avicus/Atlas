package net.avicus.atlas.core.module.shop;

import java.util.List;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.Module;
import net.avicus.atlas.core.util.Events;

@ToString(exclude = "match")
public class ShopModule implements Module {

  private final Match match;
  private final List<Shop> shops;

  public ShopModule(Match match, List<Shop> shops) {
    this.match = match;
    this.shops = shops;
  }

  @Override
  public void open() {
    this.shops.forEach(Events::register);
    this.shops.forEach(s -> Events.register(s.getPointListener()));
  }

  @Override
  public void close() {
    this.shops.forEach(Events::unregister);
    this.shops.forEach(s -> Events.unregister(s.getPointListener()));
  }
}
