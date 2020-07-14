package net.avicus.atlas.core.command;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import java.util.Iterator;
import java.util.List;
import net.avicus.atlas.core.text.Components;
import net.avicus.atlas.core.util.Translations;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.utils.Strings;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenericCommands {

  @Command(aliases = {"ping", "pong"}, desc = "Get your ping", max = 1, usage = "[player]")
  public static void ping(final CommandContext args, final CommandSender source)
      throws CommandException {
    Player target = source instanceof Player ? (Player) source : null;
    if (args.argsLength() > 0) {
      if (!source.hasPermission("atlas.command.generic.ping.other")) {
        throw new CommandPermissionsException();
      }

      target = Bukkit.getPlayer(args.getString(0), source);
    }

    if (target == null) {
      throw new TranslatableCommandErrorException(Translations.ERROR_UNKNOWN_PLAYER,
          new UnlocalizedText(args.getString(0)));
    }

    if (source instanceof Player && target.equals(source)) {
      source.sendMessage(Translations.COMMANDS_GENERIC_PING_SELF
          .with(ChatColor.GRAY, latency(((Player) source).spigot().getPing())));
    } else {
      source.sendMessage(Translations.COMMANDS_GENERIC_PING_OTHER
          .with(ChatColor.GRAY, new UnlocalizedText(target.getDisplayName(source)),
              latency(target.spigot().getPing())));
    }
  }

  @Command(aliases = {"staff", "stafflist"}, desc = "View a list of online staff", max = 0)
  public static void staffList(final CommandContext context, final CommandSender sender) {
    List<Player> online = Lists.newArrayList();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.isOp() || player.hasPermission("atlas.staff")) {
        online.add(player);
      }
    }
    sender.sendMessage(Strings.padChatComponent(
        Translations.COMMANDS_STAFF_TITLE.with(ChatColor.GREEN)
            .translate(sender.getLocale()), "-", ChatColor.YELLOW, ChatColor.AQUA));
    if (online.isEmpty()) {
      sender.sendMessage(Translations.COMMANDS_STAFF_NONE.with(ChatColor.GRAY));
      return;
    }
    final TextComponent component = new TextComponent();
    final Iterator<Player> it = online.iterator();
    while (it.hasNext()) {
      component.addExtra(it.next().getDisplayName(sender));
      if (it.hasNext()) {
        component.addExtra(Components.simple(", ", ChatColor.WHITE));
      }
    }
    sender.sendMessage(component);
  }

  private static LocalizedNumber latency(final int latency) {
    final LocalizedNumber component = new LocalizedNumber(latency);
    ChatColor color;
    if (latency < 0) {
      color = ChatColor.DARK_GREEN;
    } else if (latency < 100) {
      color = ChatColor.GREEN;
    } else if (latency < 150) {
      color = ChatColor.YELLOW;
    } else if (latency < 300) {
      color = ChatColor.GOLD;
    } else if (latency < 600) {
      color = ChatColor.RED;
    } else {
      color = ChatColor.DARK_RED;
    }
    component.style().color(color);
    return component;
  }
}
