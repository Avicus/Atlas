package net.avicus.atlas.core.module.zones.zones.filtered;

import java.util.Optional;
import lombok.ToString;
import net.avicus.atlas.core.event.world.BlockChangeByPlayerEvent;
import net.avicus.atlas.core.event.world.BlockChangeEvent;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.variable.EntityVariable;
import net.avicus.atlas.core.module.checks.variable.LocationVariable;
import net.avicus.atlas.core.module.checks.variable.MaterialVariable;
import net.avicus.atlas.core.module.checks.variable.PlayerVariable;
import net.avicus.atlas.core.module.zones.Zone;
import net.avicus.atlas.core.module.zones.ZoneMessage;
import net.avicus.atlas.core.runtimeconfig.fields.ConfigurableField;
import net.avicus.atlas.core.runtimeconfig.fields.OptionalField;
import net.avicus.atlas.core.runtimeconfig.fields.RegisteredObjectField;
import net.avicus.atlas.core.util.region.Region;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

@ToString(callSuper = true)
public class FilteredInteractionZone extends Zone {

  private Optional<Check> modify;
  private Optional<Check> blockPlace;
  private Optional<Check> blockBreak;
  private Optional<Check> use;

  public FilteredInteractionZone(Match match, Region region, Optional<ZoneMessage> message,
      Optional<Check> modify, Optional<Check> blockPlace, Optional<Check> blockBreak,
      Optional<Check> use) {
    super(match, region, message);
    this.modify = modify;
    this.blockPlace = blockPlace;
    this.blockBreak = blockBreak;
    this.use = use;
  }

  @Override
  public boolean isActive() {
    return this.modify.isPresent() ||
        this.blockPlace.isPresent() ||
        this.blockBreak.isPresent() ||
        this.use.isPresent();
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void blockChangeByPlayer(BlockChangeByPlayerEvent event) {
    this.onBlockChange(event);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockChange(BlockChangeEvent event) {
    if (!getRegion().contains(event.getBlock())) {
      return;
    }

    if (this.modify.isPresent()) {
      if (!test(event, this.modify.get())) {
        if (event instanceof BlockChangeByPlayerEvent) {
          message(((BlockChangeByPlayerEvent) event).getPlayer());
        }
        event.setCancelled(true);
      }
    }

    if (this.blockBreak.isPresent() && (event.getCause() instanceof BlockBreakEvent || event
        .getCause() instanceof EntityExplodeEvent)) {
      if (!test(event, this.blockBreak.get())) {
        if (event instanceof BlockChangeByPlayerEvent) {
          message(((BlockChangeByPlayerEvent) event).getPlayer());
        }
        event.setCancelled(true);
      }
    }

    if (this.blockPlace.isPresent() && event.getCause() instanceof BlockPlaceEvent) {
      if (!test(event, this.blockPlace.get())) {
        if (event instanceof BlockChangeByPlayerEvent) {
          message(((BlockChangeByPlayerEvent) event).getPlayer());
        }
        event.setCancelled(true);
      }
    }

  }

  @EventHandler
  public void onBucketFill(PlayerBucketFillEvent event) {
    this.onBucket(event);
  }

  @EventHandler
  public void onBucketEmpty(PlayerBucketEmptyEvent event) {
    this.onBucket(event);
  }

  private void onBucket(PlayerBucketEvent event) {
    if (!getRegion().contains(event.getBlockClicked())) {
      return;
    }

    if (this.use.isPresent() && !test(event, this.use.get())) {
      message(event.getPlayer());
      event.setCancelled(true);
    }

    if (this.blockBreak.isPresent() && event instanceof PlayerBucketFillEvent) {
      if (!test(event, this.blockBreak.get())) {
        message(event.getPlayer());
        event.setCancelled(true);
      }
    }

    if (this.blockPlace.isPresent() && event instanceof PlayerBucketEmptyEvent) {
      if (!test(event, this.blockPlace.get())) {
        message(event.getPlayer());
        event.setCancelled(true);
      }
    }

  }

  private boolean test(BlockChangeEvent event, Check check) {
    CheckContext context = new CheckContext(this.match);
    context.add(new MaterialVariable(event.getBlock().getState().getData()));
    context.add(new LocationVariable(event.getBlock().getLocation()));
    if (event instanceof BlockChangeByPlayerEvent) {
      context.add(new PlayerVariable(((BlockChangeByPlayerEvent) event).getPlayer()));
    }
    if (event.getCause() instanceof EntityExplodeEvent) {
      context.add(new EntityVariable(((EntityExplodeEvent) event.getCause()).getEntity()));
    }

    return check.test(context).passes();
  }

  private boolean test(PlayerBucketEvent event, Check check) {
    CheckContext context = new CheckContext(this.match);
    context.add(new LocationVariable(event.getBlockClicked().getLocation()));
    context.add(new PlayerVariable(event.getPlayer()));
    return check.test(context).passes();
  }

  @Override
  public ConfigurableField[] getFields() {
    return ArrayUtils.addAll(super.getFields(),
        new OptionalField<>("Modify", () -> this.modify, (v) -> this.modify = v, new RegisteredObjectField<>("modify", Check.class)),
        new OptionalField<>("Block Place", () -> this.blockPlace, (v) -> this.blockPlace = v, new RegisteredObjectField<>("place", Check.class)),
        new OptionalField<>("Block Break", () -> this.blockBreak, (v) -> this.blockBreak = v, new RegisteredObjectField<>("break", Check.class)),
        new OptionalField<>("Use", () -> this.use, (v) -> this.use = v, new RegisteredObjectField<>("use", Check.class))
    );
  }

  @Override
  public String getDescription(CommandSender viewer) {
    return "Filtered Interaction" + super.getDescription(viewer);
  }
}
