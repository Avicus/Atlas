package net.avicus.atlas.core.util.region.shapes;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import lombok.ToString;
import net.avicus.atlas.core.util.region.BoundedRegion;
import net.avicus.atlas.core.util.region.Region;
import org.bukkit.util.Vector;

@ToString
public class BlockRegion implements Region, BoundedRegion {

  private final Vector vector;

  public BlockRegion(Vector vector) {
    this.vector = vector.setX(vector.getBlockX()).setY(vector.getBlockY()).setZ(vector.getBlockZ());
  }

  @Override
  public boolean contains(Vector vector) {
    if (vector.getBlockX() == this.vector.getBlockX()) {
      if (vector.getBlockY() == this.vector.getBlockY()) {
        return vector.getBlockZ() == this.vector.getBlockZ();
      }
    }
    return false;
  }

  @Override
  public Vector getRandomPosition(Random random) {
    return this.vector.clone()
        .add(new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble()));
  }

  @Override
  public Vector getMin() {
    return this.vector.clone();
  }

  @Override
  public Vector getMax() {
    return this.vector.clone();
  }

  @Override
  public Iterator<Vector> iterator() {
    return Collections.singletonList(this.vector).iterator();
  }
}
