package net.avicus.atlas.core.chat;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.avicus.atlas.core.component.CommandComponent;
import net.avicus.atlas.core.network.user.Users;
import net.avicus.atlas.core.util.Events;
import net.avicus.atlas.core.util.Messages;
import net.avicus.compendium.commands.exception.MustBePlayerCommandException;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.PlayerSettings;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.types.SettingTypes;
import net.avicus.compendium.sound.SoundEvent;
import net.avicus.compendium.sound.SoundLocation;
import net.avicus.compendium.sound.SoundType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommands implements CommandComponent {

  final static Setting<PrivateMessageScope> PRIVATE_MESSAGES_SETTING = new Setting<>(
      "private-messages",
      SettingTypes.enumOf(PrivateMessageScope.class),
      PrivateMessageScope.ALL,
      Messages.PRIVATE_MESSAGES,
      Messages.PRIVATE_MESSAGES_SUMMARY
  );
  private final static Map<UUID, UUID> lastMessages = new HashMap<>();

  private static boolean shouldDeliver(Player from, Player to) {
    PrivateMessageScope option = PlayerSettings.get(to.getUniqueId(), PRIVATE_MESSAGES_SETTING);
    switch (option) {
      case ALL:
        return true;
      case OFF:
        return false;
    }
    return true;
  }

  public static Optional<Player> getLastMessaged(UUID uuid) {
    UUID last = lastMessages.get(uuid);
    if (last == null) {
      return Optional.empty();
    }
    Player player = Bukkit.getPlayer(last);
    return Optional.ofNullable(player);
  }

  @Command(aliases = {"message",
      "msg"}, min = 2, desc = "Privately message a player.", usage = "<player> <message...>")
  public static void message(CommandContext cmd, CommandSender sender)
      throws MustBePlayerCommandException {
    MustBePlayerCommandException.ensurePlayer(sender);

    Player player = (Player) sender;

    String query = cmd.getString(0);
    String body = cmd.getJoinedStrings(1);

    Player to = Bukkit.getPlayer(query, sender);

    if (to == null) {
      sender.sendMessage(Messages.ERROR_NO_PLAYERS.with(ChatColor.RED));
      return;
    }

    deliver(player, to, body);
  }

  @Command(aliases = {"reply", "r",
      "re"}, min = 1, desc = "Privately reply to a player's message.", usage = "<message...>")
  public static void reply(CommandContext cmd, CommandSender sender)
      throws MustBePlayerCommandException {
    MustBePlayerCommandException.ensurePlayer(sender);

    Player player = (Player) sender;
    Player target = getLastMessaged(player.getUniqueId()).orElse(null);

    if (target == null) {
      sender.sendMessage(Messages.ERROR_NO_REPLY.with(ChatColor.RED));
      return;
    }

    String body = cmd.getJoinedStrings(0);

    if (!target.isOnline()) {
      player.sendMessage(
          Messages.UI_NOT_ONLINE.with(ChatColor.RED, target.getName()));
      return;
    }

    deliver(player, target, body);
  }

  private static void deliver(Player from, Player to, String message) {
    lastMessages.put(from.getUniqueId(), to.getUniqueId());
    lastMessages.put(to.getUniqueId(), from.getUniqueId());

    Localizable body = new UnlocalizedText(message, ChatColor.WHITE);

    Localizable toName = Users.getLocalizedDisplay(to);
    from.sendMessage(
        Messages.GENERIC_MESSAGE_TO.with(ChatColor.GRAY, toName, body));

    // Check if player allows messages from that user
    if (shouldDeliver(from, to)) {
      Localizable fromName = Users.getLocalizedDisplay(from, true);
      to.sendMessage(
          Messages.GENERIC_MESSAGE_FROM.with(ChatColor.GRAY, fromName, body));
      SoundEvent call = Events
          .call(new SoundEvent(to, SoundType.SNARE, SoundLocation.PRIVATE_MESSAGE));
      call.getSound().play(to, 1F);
    }

  }

  @Override
  public void enable() {
    PlayerSettings.register(PRIVATE_MESSAGES_SETTING);
  }

  @Override
  public void registerCommands(CommandsManagerRegistration registrar) {
    registrar.register(MessageCommands.class);
  }

  public enum PrivateMessageScope {
    ALL,
    FRIENDS,
    OFF
  }
}
