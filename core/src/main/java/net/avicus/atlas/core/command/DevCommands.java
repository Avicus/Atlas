package net.avicus.atlas.core.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import java.util.Optional;
import java.util.UUID;
import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.map.AtlasMap;
import net.avicus.atlas.core.map.library.MapLibrary;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.checks.Check;
import net.avicus.atlas.core.module.checks.CheckContext;
import net.avicus.atlas.core.module.checks.CheckResult;
import net.avicus.atlas.core.module.checks.variable.PlayerVariable;
import net.avicus.atlas.core.util.Messages;
import net.avicus.compendium.Paste;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevCommands {

  @Command(aliases = {"reloadlibrary", "refreshlibrary"}, desc = "Reload the map libraries.")
  @CommandPermissions("atlas.dev.reloadlibrary")
  public static void reloadlibrary(CommandContext cmd, final CommandSender sender) {
    for (MapLibrary library : Atlas.get().getMapManager().getLibraries()) {
      library.build();
    }
    sender.sendMessage(Messages.GENERIC_LIBRARIES_RELOADED.with(ChatColor.GOLD));
  }

  @Command(aliases = "testall", desc = "Try to parse every map in the loaded libraries.")
  @CommandPermissions("atlas.dev.testall")
  public static void testAll(CommandContext cmd, final CommandSender sender) {
    sender.sendMessage(
        "Beginning Parsing (This may take a while) \n Keep in mind that only errors will be displayed.");
    parseAll(sender);
  }

  @Command(aliases = "dump", desc = "Dump Atlas variables to a paste.")
  @CommandPermissions("atlas.dev.dump")
  public static void dump(CommandContext cmd, CommandSender sender) {
    String title = String.format("Atlas Dump %s", UUID.randomUUID().toString().substring(0, 6));
    String text = "Match: \n" +
        Atlas.getMatch() +
        "\n\n\n" +
        "Players: \n" +
        Bukkit.getOnlinePlayers() +
        "\n\n\n" +
        "Map libraries: \n" +
        Atlas.get().getMapManager().getLibraries();

    String url = new Paste(title, "*Console", text, true).upload();

    sender.sendMessage(Messages.GENERIC_DUMP.with(ChatColor.GOLD, url));
  }

  @Command(aliases = "check", desc = "Run a check.", usage = "<check-id>")
  @CommandPermissions("atlas.dev.check")
  public static void check(CommandContext cmd, CommandSender sender) throws CommandException {
    Match match = Atlas.getMatch();
    Check check = match.getRegistry().get(Check.class, cmd.getJoinedStrings(0), true).orElse(null);
    if (check == null) {
      throw new CommandException("Check not found.");
    }
    CheckContext context = new CheckContext(match);
    if (sender instanceof Player) {
      context.add(new PlayerVariable((Player) sender));
    }

    CheckResult result = check.test(context);
    switch (result) {
      case ALLOW:
        sender.sendMessage(ChatColor.GREEN + "ALLOW");
        return;
      case DENY:
        sender.sendMessage(ChatColor.RED + "DENY");
        return;
      case IGNORE:
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "IGNORE");
    }
  }

  private static void parseAll(CommandSender sender) {
    int success = 0;
    int tried = 0;
    for (MapLibrary library : Atlas.get().getMapManager().getLibraries()) {
      for (AtlasMap source : library.getMaps()) {
        tried++;
        Optional<Match> match = parse(sender, source);
        if (match.isPresent()) {
          success++;
        }
      }
    }
    sender.sendMessage(
        "PARSING FINISHED: \n" + "(" + success + "/" + tried + ") maps parsed successfully.");
  }

  private static Optional<Match> parse(CommandSender sender, AtlasMap map) {
    try {
      return Optional.of(Atlas.get().getMatchManager().getFactory().create(map));
    } catch (Exception e) {
      String folder = map.getName();
      sender.sendMessage(Messages.ERROR_PARSING_FAILED.with(ChatColor.RED, folder));

      Localizable error = new UnlocalizedText(e.getMessage());

      error.style().color(ChatColor.RED);

      sender.sendMessage(error);
      return Optional.empty();
    }
  }

  @Command(aliases = {
      "permissions"}, desc = "View permissions for a player and which plugin assigned them.", max = 1)
  public static void permissions(CommandContext cmd, CommandSender sender) {
    String query = cmd.getString(0);
    Player search = Bukkit.getPlayer(query);

    if (search == null) {
      sender.sendMessage(Messages.ERROR_NO_PLAYERS.with(ChatColor.RED));
      return;
    }

    sender.sendMessage(ChatColor.GOLD + "Permissions for " + search.getDisplayName());
    search.getEffectivePermissions().forEach(attachment -> {
      if (attachment.getAttachment() == null) {
        return;
      }

      String assigner = "Bukkit";
      if (attachment.getAttachment().getPlugin() != null) {
        assigner = attachment.getAttachment().getPlugin().getName();
      }
      String permission = attachment.getPermission();
      ChatColor color = attachment.getValue() ? ChatColor.GREEN : ChatColor.RED;

      sender.sendMessage(ChatColor.AQUA + assigner + " - " + color + permission);
    });
  }

  @Command(aliases = {"has-permission",
      "hp"}, desc = "Check if a player has a permission.", max = 2, usage = "<player> <permission>")
  public static void hasPerm(CommandContext cmd, CommandSender sender) {
    String query = cmd.getString(0);
    Player search = Bukkit.getPlayer(query);

    if (search == null) {
      sender.sendMessage(Messages.ERROR_NO_PLAYERS.with(ChatColor.RED));
      return;
    }

    sender.sendMessage(
        search.hasPermission(cmd.getString(1)) ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO");
  }

  public static class Parent {

    @CommandPermissions("hook.dev")
    @Command(aliases = {"dev"}, desc = "Development commands")
    @NestedCommand(DevCommands.class)
    public static void parent(CommandContext args, CommandSender source) {
    }
  }
}
