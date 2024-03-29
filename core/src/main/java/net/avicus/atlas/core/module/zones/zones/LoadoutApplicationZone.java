package net.avicus.atlas.core.module.zones.zones;

import java.util.Optional;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.loadouts.Loadout;
import net.avicus.atlas.core.module.zones.Zone;
import net.avicus.atlas.core.module.zones.ZoneMessage;
import net.avicus.atlas.core.runtimeconfig.fields.ConfigurableField;
import net.avicus.atlas.core.runtimeconfig.fields.RegisteredObjectField;
import net.avicus.atlas.core.util.region.Region;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import tc.oc.tracker.event.PlayerCoarseMoveEvent;

@ToString(callSuper = true)
public class LoadoutApplicationZone extends Zone {

  private Loadout loadout;

  public LoadoutApplicationZone(Match match, Region region, Optional<ZoneMessage> message,
      Loadout loadout) {
    super(match, region, message);
    this.loadout = loadout;
  }

  @Override
  public boolean isActive() {
    return loadout != null;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onTP(PlayerTeleportEvent event) {
    handle(event.getPlayer(), event.getFrom(), event.getTo());
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onVelocityZoneEnter(PlayerCoarseMoveEvent event) {
    handle(event.getPlayer(), event.getFrom(), event.getTo());
  }

  public void handle(Player player, Location fromLoc, Location toLoc) {
    if (isObserving(this.match, player)) {
      return;
    }

    boolean from = getRegion().contains(fromLoc);

    if (from) {
      return;
    }

    boolean to = getRegion().contains(toLoc);

    if (!to) {
      return;
    }

    this.loadout.apply(player);
  }

  @Override
  public String getDescription(CommandSender viewer) {
    return "Loadout Application" + super.getDescription(viewer);
  }

  @Override
  public ConfigurableField[] getFields() {
    return ArrayUtils.addAll(super.getFields(), new RegisteredObjectField<>("Loadout", () -> this.loadout, (v) -> this.loadout = v, Loadout.class));
  }
}
