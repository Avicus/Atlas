package net.avicus.atlas.core.announce;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class AnnounceCommands {

  private static TextComponent prefixNormal = new TextComponent(
      org.bukkit.ChatColor.GOLD + "[" + org.bukkit.ChatColor.AQUA + org.bukkit.ChatColor.BOLD
          + "AVN" + org.bukkit.ChatColor.GOLD + "] ");
  private static TextComponent prefixCritical = new TextComponent(
      org.bukkit.ChatColor.RED + "[" + org.bukkit.ChatColor.GOLD + org.bukkit.ChatColor.BOLD
          + "NETWORK ALERT" + org.bukkit.ChatColor.RED
          + "] ");

  @Command(aliases = {
      "message"}, desc = "Announce a message to the network.", min = 1, usage = "<message>")
  public static void message(CommandContext cmd, CommandSender sender) throws CommandException {
    send(cmd.getJoinedStrings(0), AnnounceType.MESSAGE);
    sender.sendMessage("Message sent!");
  }

  @Command(aliases = {
      "critical"}, desc = "Announce a critical message to the network.", min = 1, usage = "<message>")
  public static void critical(CommandContext cmd, CommandSender sender) throws CommandException {
    if (!(sender.isOp() || sender instanceof ConsoleCommandSender)) {
      throw new CommandPermissionsException();
    }
    send(cmd.getJoinedStrings(0), AnnounceType.CRITICAL);
    sender.sendMessage("Message sent!");
  }

  public enum AnnounceType {
    NO_PREFIX, MESSAGE, CRITICAL
  }

  private static void send(String message, AnnounceType type) {
    TextComponent prefix;
    if (type == AnnounceType.NO_PREFIX) {
      prefix = new TextComponent();
    } else if (type == AnnounceType.CRITICAL) {
      prefix = prefixCritical;
    } else {
      prefix = prefixNormal;
    }

    BaseComponent send = new TextComponent(prefix,
        new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    Bukkit.broadcast(send);
  }

  public static class Parent {

    @CommandPermissions("hook.announce")
    @Command(aliases = {"announce"}, desc = "announcement commands")
    @NestedCommand(AnnounceCommands.class)
    public static void parent(CommandContext args, CommandSender source) {
    }
  }
}
