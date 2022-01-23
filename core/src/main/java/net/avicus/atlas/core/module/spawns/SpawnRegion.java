package net.avicus.atlas.core.module.spawns;

import java.util.Optional;
import java.util.Random;
import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.runtimeconfig.RuntimeConfigurable;
import net.avicus.atlas.core.runtimeconfig.fields.AngleProviderField;
import net.avicus.atlas.core.runtimeconfig.fields.ConfigurableField;
import net.avicus.atlas.core.runtimeconfig.fields.OptionalField;
import net.avicus.atlas.core.runtimeconfig.fields.RegisteredObjectField;
import net.avicus.atlas.core.util.region.BoundedRegion;
import net.avicus.compendium.points.AngleProvider;
import org.bukkit.util.Vector;
import org.bukkit.command.CommandSender;

@ToString
public class SpawnRegion implements RuntimeConfigurable {

  @Getter
  private BoundedRegion region;
  @Getter
  private Optional<AngleProvider> yaw;
  @Getter
  private Optional<AngleProvider> pitch;

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

  @Override
  public String getDescription(CommandSender viewer) {
    return "Spawn Region";
  }

  @Override
  public ConfigurableField[] getFields() {
    return new ConfigurableField[]{
        new RegisteredObjectField<>("Region", () -> this.region, (v) -> this.region = v, BoundedRegion.class),
        new OptionalField<>("Yaw", () -> this.yaw, (v) -> this.yaw = v, new AngleProviderField("yaw")),
        new OptionalField<>("Pitch", () -> this.pitch, (v) -> this.pitch = v, new AngleProviderField("yaw"))
    };
  }
}
