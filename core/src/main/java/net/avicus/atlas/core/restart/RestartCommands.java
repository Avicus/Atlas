package net.avicus.atlas.core.restart;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import org.bukkit.command.CommandSender;

public class RestartCommands {

  public static RestartHandler HANDLER = new BukkitRestartHandler();

  @Command(aliases = {"queuerestart", "qr"}, desc = "Queue a restart after the match.")
  @CommandPermissions("atlas.dev.queuerestart")
  public static void queueRestart(CommandContext cmd, CommandSender sender)
      throws CommandPermissionsException {
    HANDLER.queue();
    sender.sendMessage("Restart queued.");
  }
}
