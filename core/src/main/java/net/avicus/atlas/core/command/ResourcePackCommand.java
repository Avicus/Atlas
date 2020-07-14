package net.avicus.atlas.core.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.command.exception.CommandMatchException;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.resourcepacks.RequestResourcePackModule;
import net.avicus.atlas.core.util.Messages;
import net.avicus.compendium.commands.exception.MustBePlayerCommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResourcePackCommand {

  @Command(aliases = {"resource-pack",
      "resourcepack"}, desc = "Download the custom resource pack for a map.", max = 0)
  public static void resourcepack(CommandContext cmd, CommandSender sender) throws Exception {
    MustBePlayerCommandException.ensurePlayer(sender);

    Match match = Atlas.getMatch();

    if (match == null) {
      throw new CommandMatchException();
    }

    RequestResourcePackModule module = match.getModule(RequestResourcePackModule.class)
        .orElse(null);

    if (module == null) {
      sender.sendMessage(Messages.ERROR_NO_RESOURCE_PACK.with(ChatColor.RED));
      return;
    }

    module.request((Player) sender);
  }
}
