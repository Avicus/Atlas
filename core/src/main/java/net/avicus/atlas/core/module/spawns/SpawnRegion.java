package net.avicus.atlas.core.module.spawns;

import java.util.Optional;
import java.util.Random;
import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.util.region.BoundedRegion;
import net.avicus.compendium.points.AngleProvider;
import org.bukkit.util.Vector;

@ToString
public class SpawnRegion {

  @Getter
  private final BoundedRegion region;
  @Getter
  private final Optional<AngleProvider> yaw;
  @Getter
  private final Optional<AngleProvider> pitch;

  public SpawnRegion(BoundedRegion region, Optional<AngleProvider> yaw,
      Optional<AngleProvider> pitch) {
    this.region = region;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public Vector randomPosition(Random random) {
    return this.region.getRandomPosition(random);
  }

  public Vector getCenter() {
    return this.region.getCenter();
  }
}
