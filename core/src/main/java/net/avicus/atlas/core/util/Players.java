package net.avicus.atlas.core.util;

import net.avicus.atlas.core.Atlas;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class Players {

  public static void setData(Entity entity, String key, Object value) {
    entity.setMetadata(key, new FixedMetadataValue(Atlas.get(), value));
  }

  public static String getDataString(Entity entity, String key, String def) {
    if (!entity.hasMetadata(key)) {
      return def;
    }
    return entity.getMetadata(key).get(0).asString();
  }

  public static boolean getDataBoolean(Entity entity, String key, boolean def) {
    if (!entity.hasMetadata(key)) {
      return def;
    }
    return entity.getMetadata(key).get(0).asBoolean();
  }

  public static void playFireworkSound() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1.0F, 1.0F);
    }
  }

  public static void reset(Player player) {
    player.setGameMode(GameMode.SURVIVAL);
    player.setAllowFlight(false);
    player.setFlying(false);
    player.setFlySpeed(0.1F);
    player.setWalkSpeed(0.2F);
    player.setExp(0);
    player.setTotalExperience(0);
    player.setLevel(0);
    player.setMaxHealth(20);
    player.setHealth(20);
    player.setFoodLevel(20);
    player.setSaturation(5);
    player.setCanPickupItems(true);
    player.setFireTicks(0);
    player.setRemainingAir(20);
    player.setFallDistance(0);
    player.setVelocity(new Vector());
    player.resetPlayerWeather();
    player.resetPlayerTime();
    player.setItemOnCursor(null);

    player.eject();
    if (player.getVehicle() != null) {
      player.getVehicle().eject();
    }

    // Inventory
    player.closeInventory();
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    player.updateInventory();
    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }

    // Spigot
    player.spigot().setCollidesWithEntities(true);
    player.spigot().setAffectsSpawning(true);
  }
}