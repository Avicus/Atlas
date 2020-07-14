package net.avicus.atlas.core.util.region.modifiers;

import java.util.Random;
import lombok.ToString;
import net.avicus.atlas.core.util.region.BoundedRegion;
import net.avicus.atlas.core.util.region.Region;
import org.bukkit.util.Vector;

@ToString
public class TranslateRegion implements Region {

  private final Region child;
  private final Vector offset;

  public TranslateRegion(Region child, Vector offset) {
    this.child = child;
    this.offset = offset;
  }

  public Vector getOffset() {
    return this.offset.clone();
  }

  @Override
  public boolean contains(Vector vector) {
    Vector test = vector.clone().subtract(this.offset);
    return this.child.contains(test);
  }

  @Override
  public Vector getRandomPosition(Random random) {
    if (this.child instanceof BoundedRegion) {
      return this.child.getRandomPosition(random).clone().subtract(offset);
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
