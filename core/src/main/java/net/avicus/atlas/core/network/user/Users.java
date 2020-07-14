package net.avicus.atlas.core.network.user;

import com.sk89q.minecraft.util.commands.ChatColor;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.Nullable;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Users {

  public static Optional<Player> player(CommandSender sender) {
    if (sender instanceof Player) {
      return Optional.of((Player) sender);
    }
    return Optional.empty();
  }

  public static UnlocalizedText getLocalizedDisplay(CommandSender sender) {
    return getLocalizedDisplay(sender, false);
  }

  @Nullable
  public static UnlocalizedText getPrefix(CommandSender sender) {
    String prefix = "";

    if (!prefix.isEmpty()) {
      return new UnlocalizedText(prefix);
    } else {
      return null;
    }
  }

  @Nullable
  public static UnlocalizedText getSuffix(CommandSender sender) {
    String suffix = "";

    if (!suffix.isEmpty()) {
      return new UnlocalizedText(suffix);
    } else {
      return null;
    }
  }

  /**
   * zz Get the display of an online or offline player (rank, badges, team color).
   */
  public static UnlocalizedText getLocalizedDisplay(CommandSender sender, boolean forceRanks) {
    String name = "";

    if (sender instanceof Player) {
      name = name + ChatColor.DARK_AQUA + ((Player) sender).getDisplayName();
    } else {
      name = name + ChatColor.DARK_AQUA + "Console";
    }

    UnlocalizedText prefix = getPrefix(sender);
    UnlocalizedText suffix = getSuffix(sender);

    // Prefix Name Suffix
    UnlocalizedFormat format = new UnlocalizedFormat("{0}{1}{2}");
    return format.with(prefix, new UnlocalizedText(name), suffix);
  }

  public static String getDisplay(CommandSender player) {
    return getDisplay(player, false);
  }

  public static String getDisplay(CommandSender player, boolean forceRanks) {
    return getLocalizedDisplay(player, forceRanks).translate(Locale.ENGLISH).toLegacyText();
  }
}
