package net.avicus.atlas.core.util.region.special;

import java.util.Optional;
import java.util.Random;
import lombok.ToString;
import net.avicus.atlas.core.util.region.Region;
import org.bukkit.util.Vector;

@ToString
public class BelowRegion implements Region {

  private final Optional<Integer> x;
  private final Optional<Integer> y;
  private final Optional<Integer> z;

  public BelowRegion(Optional<Integer> x, Optional<Integer> y, Optional<Integer> z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public boolean contains(Vector vector) {
    boolean x = !this.x.isPresent() || vector.getX() < this.x.get();
    boolean y = !this.y.isPresent() || vector.getY() < this.y.get();
    boolean z = !this.z.isPresent() || vector.getZ() < this.z.get();

    return x && y && z;
  }

  @Override
  public Vector getRandomPosition(Random random) {
    int x = this.x.orElse(random.nextInt());
    int y = this.x.orElse(random.nextInt());
    int z = this.x.orElse(random.nextInt());
    return new Vector(x, y, z).subtract(new Vector(
        this.x.isPresent() ? random.nextInt() : 0,
        this.y.isPresent() ? random.nextInt() : 0,
        this.z.isPresent() ? random.nextInt() : 0)
    );
  }
}
