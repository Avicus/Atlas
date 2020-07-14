package net.avicus.atlas.core.channel.report;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.avicus.atlas.core.AtlasConfig;
import net.avicus.atlas.core.channel.PrefixedChannel;
import net.avicus.atlas.core.util.Translations;
import net.avicus.atlas.core.text.Components;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.settings.PlayerSettings;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.types.SettingTypes;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.joda.time.Duration;
import org.joda.time.Instant;

public class ReportChannel extends PrefixedChannel {

  static final Setting<Boolean> REPORT_NOTIFICATION_SETTING = new Setting<>(
      "report-notification",
      SettingTypes.BOOLEAN,
      true,
      Translations.SETTING_REPORTNOTIFICATION_NAME.with(),
      Translations.SETTING_REPORTNOTIFICATION_SUMMARY.with()
  );
  static final String ID = "report";
  private final Map<Player, AtomicInteger> counts = new WeakHashMap<>();
  private final Map<Player, Instant> lastReport = new WeakHashMap<>();

  ReportChannel() {
    super(ID, "channels.report", channelDescriptor("Report", ChatColor.RED));
  }

  private static ChatColor colorForReports(int reports) {
    if (reports >= 5) {
      return ChatColor.DARK_RED;
    }

    switch (reports) {
      case 1:
      case 2:
        return ChatColor.GREEN;
      case 3:
        return ChatColor.YELLOW;
      default:
        return ChatColor.RED;
    }
  }

  void report(Player source, Player target, String reason)
      throws TranslatableCommandErrorException {
    this.checkCooldown(source);
    final int reports = this.counts.computeIfAbsent(target, x -> new AtomicInteger())
        .incrementAndGet();

    TextComponent toSend = new TextComponent("");

    toSend.addExtra(source.getDisplayName());
    toSend.addExtra(Components.simple(" » ", ChatColor.GRAY));
    toSend.addExtra(target.getDisplayName());
    toSend.addExtra(Components.simple(": ", ChatColor.GRAY));
    toSend.addExtra(Components.simple(reason, ChatColor.WHITE));
    toSend.addExtra(Components.simple(" [", ChatColor.GRAY));
    toSend.addExtra(Components.simple(String.valueOf(reports), colorForReports(reports)));
    toSend.addExtra(Components.simple("]", ChatColor.GRAY));
    this.send(source, toSend);
  }

  private void checkCooldown(Player source) throws TranslatableCommandErrorException {
    Instant instant = this.lastReport.get(source);
    Duration duration = AtlasConfig.Channel.Report.getCooldown()
        .minus(new Duration(instant, Instant.now()));
    if (instant != null && duration.isLongerThan(Duration.ZERO)) {
      throw new TranslatableCommandErrorException(
          (duration.getStandardSeconds() > 1 ? Translations.REPORT_COOLDOWN_PLURAL
              : Translations.REPORT_COOLDOWN_SINGULAR),
          new LocalizedNumber(duration.getStandardSeconds()));
    }

    this.lastReport.put(source, Instant.now());
  }

  @Override
  protected void preSend(final Player viewer) {
    if (PlayerSettings.get(viewer, REPORT_NOTIFICATION_SETTING)) {
      viewer.playSound(viewer.getLocation(), Sound.CAT_HISS, 1F, 2F);
    }
  }
}
