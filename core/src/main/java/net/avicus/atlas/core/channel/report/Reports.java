package net.avicus.atlas.core.channel.report;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.component.CommandComponent;
import net.avicus.atlas.core.util.Translations;
import net.avicus.compendium.commands.exception.MustBePlayerCommandException;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reports implements CommandComponent {

  private final ReportChannel channel = new ReportChannel();

  private static ReportChannel getChannel() {
    return Atlas.get().getChannelManager().getChannel(ReportChannel.ID, ReportChannel.class);
  }

  @Command(aliases = {"report"}, desc = "Report a player.", usage = "<player> <reason...>", min = 2)
  public static void report(final CommandContext context, final CommandSender sender)
      throws TranslatableCommandErrorException {
    final Player source = MustBePlayerCommandException.ensurePlayer(sender);
    final Player target = Bukkit.getPlayer(context.getString(0));
    if (target == null) {
      sender.sendMessage(Translations.ERROR_UNKNOWN_PLAYER
          .with(ChatColor.RED, new UnlocalizedText(context.getString(0))));
      return;
    }

    getChannel().report(source, target, context.getJoinedStrings(1));
    sender.sendMessage(Translations.REPORT_SENT.with(ChatColor.YELLOW));
  }

  @Override
  public void enable() {
    PlayerSettings.register(ReportChannel.REPORT_NOTIFICATION_SETTING);
    Atlas.get().getChannelManager().register(this.channel);
  }

  @Override
  public void registerCommands(CommandsManagerRegistration registrar) {
    registrar.register(Reports.class);
  }
}
