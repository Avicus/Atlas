package net.avicus.atlas.core.component.util;

import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.component.Component;
import net.avicus.atlas.core.util.AtlasTask;
import org.bukkit.entity.Arrow;

public class ArrowRemovalComponent extends AtlasTask implements Component {

  private final int FIRE_DELAY = 20 * 50;
  private final int DELAY = 20 * 120;

  @Override
  public void enable() {
    this.repeat(0, 120); // Every 2 seconds
  }

  @Override
  public void disable() {
    this.cancel0();
  }

  @Override
  public void run() {
    Atlas.performOnMatch(m -> {
      m.getWorld().getEntitiesByClass(Arrow.class).forEach(a -> {
        if (a.getFireTicks() > FIRE_DELAY || a.getTicksLived() > DELAY) {
          a.remove();
        }
      });
    });
  }
}
