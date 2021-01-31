package net.avicus.atlas.core;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandNumberFormatException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.avicus.atlas.core.afk.AFKKickTask;
import net.avicus.atlas.core.announce.AnnounceCommands;
import net.avicus.atlas.core.channel.ChannelManager;
import net.avicus.atlas.core.channel.premium.Premium;
import net.avicus.atlas.core.channel.staff.StaffChannels;
import net.avicus.atlas.core.chat.Chat;
import net.avicus.atlas.core.chat.MessageCommands;
import net.avicus.atlas.core.command.ChannelCommands;
import net.avicus.atlas.core.command.DevCommands.Parent;
import net.avicus.atlas.core.command.GameCommands;
import net.avicus.atlas.core.command.GenericCommands;
import net.avicus.atlas.core.command.GroupCommands.GroupParentCommand;
import net.avicus.atlas.core.command.JoinCommands;
import net.avicus.atlas.core.command.KitCommands;
import net.avicus.atlas.core.command.LoadoutCommands.ParentCommand;
import net.avicus.atlas.core.command.ResourcePackCommand;
import net.avicus.atlas.core.command.RotationCommands;
import net.avicus.atlas.core.command.StateCommands;
import net.avicus.atlas.core.command.WorldEditCommands;
import net.avicus.atlas.core.command.exception.CommandMatchException;
import net.avicus.atlas.core.component.ComponentManager;
import net.avicus.atlas.core.component.dev.DebuggingComponent;
import net.avicus.atlas.core.component.freeze.FreezeComponent;
import net.avicus.atlas.core.component.util.ArrowRemovalComponent;
import net.avicus.atlas.core.component.visual.MapNotificationComponent;
import net.avicus.atlas.core.component.visual.SidebarComponent;
import net.avicus.atlas.core.component.visual.SoundComponent;
import net.avicus.atlas.core.component.visual.TabListComponent;
import net.avicus.atlas.core.component.visual.VisualEffectComponent;
import net.avicus.atlas.core.external.ModuleSet;
import net.avicus.atlas.core.external.SetLoader;
import net.avicus.atlas.core.item.LockingSharingListener;
import net.avicus.atlas.core.listener.AtlasListener;
import net.avicus.atlas.core.listener.BlockChangeListener;
import net.avicus.atlas.core.listener.EntityChangeListener;
import net.avicus.atlas.core.logging.ChatLogHandler;
import net.avicus.atlas.core.map.MapManager;
import net.avicus.atlas.core.map.rotation.RandomRotationProvider;
import net.avicus.atlas.core.map.rotation.Rotation;
import net.avicus.atlas.core.map.rotation.RotationProvider;
import net.avicus.atlas.core.map.rotation.XmlRotationProvider;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.atlas.core.match.MatchManager;
import net.avicus.atlas.core.module.kills.DeathMessage;
import net.avicus.atlas.core.module.observer.ObserverCommands;
import net.avicus.atlas.core.module.vote.VoteCommands;
import net.avicus.atlas.core.module.vote.VoteModule;
import net.avicus.atlas.core.restart.RestartCommands;
import net.avicus.atlas.core.util.AtlasBridge;
import net.avicus.atlas.core.util.AtlasTask;
import net.avicus.atlas.core.util.Events;
import net.avicus.atlas.core.util.Messages;
import net.avicus.atlas.core.util.Translations;
import net.avicus.atlas.core.util.properties.BlockPropStore;
import net.avicus.compendium.AvicusBukkitCommandManager;
import net.avicus.compendium.AvicusCommandsManager;
import net.avicus.compendium.commands.AvicusCommandsRegistration;
import net.avicus.compendium.commands.exception.AbstractTranslatableCommandException;
import net.avicus.compendium.config.Config;
import net.avicus.compendium.locale.TranslationProvider;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Atlas extends JavaPlugin {

  private static Atlas instance;
  private AtomicBoolean ready = new AtomicBoolean(false);
  private AvicusCommandsManager commandManager;
  @Getter
  @Setter
  private AtlasBridge bridge = new AtlasBridge.SimpleAtlasBridge();
  @Getter
  private MapManager mapManager;
  @Getter
  private MatchManager matchManager;

  @Getter
  private Logger mapErrorLogger;

  @Getter
  private SidebarComponent sideBar;

  @Getter
  private ComponentManager componentManager;

  @Getter
  private SetLoader loader;

  @Getter
  private MatchFactory matchFactory;

  @Getter
  private AvicusCommandsRegistration registrar;

  @Getter
  private ChannelManager channelManager = new ChannelManager();

  @Nullable
  public static Match getMatch() {
    return get().getMatchManager().getRotation().getMatch();
  }

  public static void performOnMatch(Consumer<Match> consumer) {
    Match match = getMatch();
    if (match != null) {
      consumer.accept(match);
    }
  }

  public static Atlas get() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    Bukkit.getPluginManager().registerEvents(new Listener() {
      @EventHandler(priority = EventPriority.LOWEST)
      public void onPlayerLogin(PlayerLoginEvent event) {
        if (ready.get()) {
          return;
        }
        String error = ChatColor.RED + "The server has not loaded, please rejoin in a moment.";
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, error);
      }
    }, this);

    this.saveDefaultConfig();
    this.reloadConfig();

    final Config config;
    try {
      config = new Config(new FileInputStream(new File(this.getDataFolder(), "config.yml")));
    } catch (FileNotFoundException e) {
      this.getLogger().log(Level.SEVERE, "Could not load configuration", e);
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }
    config.injector(AtlasConfig.class).inject();

    if (Translations.TYPE_BOOLEAN_FALSE == TranslationProvider.$NULL$) {
      this.getLogger().severe("Failed to pre-load.");
      this.getPluginLoader().disablePlugin(this);
      return;
    }

    this.mapErrorLogger = Logger.getLogger("map-error");
    this.mapErrorLogger.setUseParentHandlers(false);
    this.mapErrorLogger.addHandler(new ChatLogHandler("MAPS", "atlas.maperrors"));

    this.mapManager = new MapManager();
    this.mapManager.loadLibraries(AtlasConfig.getLibraries());
    this.matchFactory = new MatchFactory();

    this.commandManager = new AvicusBukkitCommandManager();
    this.registrar = new AvicusCommandsRegistration(this, this.commandManager);

    this.registerComponents();
    this.registerListeners();
    this.registerCommands(registrar);
    Chat.init();

    this.loader = new SetLoader(new File(this.getDataFolder(), "module-sets"));
    Bukkit.getLogger().info("Beginning external module set loading...");
    this.loader.loadModules();
    this.loader.getLoadedModules().forEach(m -> {
      try {
        ModuleSet set = m.getModuleInstance();

        set.setAtlas(this);
        set.setMatchFactory(this.matchFactory);
        Logger logger = Logger.getLogger(m.getDescriptionFile().getName());
        logger.setParent(this.getLogger());
        set.setLogger(logger);
        set.setRegistrar(this.registrar);

        set.onEnable();
      } catch (Exception e) {
        Bukkit.getLogger().info("Failed to load module!");
        e.printStackTrace();
      }
      Bukkit.getLogger().info("Loaded Component Set: " + m.getDescriptionFile().getName());
    });
    Bukkit.getLogger().info(
        "Finished external module set loading! Loaded " + this.loader.getLoadedModules().size()
            + " modules!");

    this.loader.getLoadedModules()
        .forEach(m -> m.getModuleInstance().onComponentsEnable(this.componentManager));

    this.componentManager.enable();

    final RotationProvider rotationProvider = new XmlRotationProvider(
        new File(AtlasConfig.getRotationFile()), this.mapManager, this.matchFactory);
    this.getLogger().info("Using " + rotationProvider.getClass().getName() + " rotation provider");
    Rotation rotation;
    try {
      rotation = rotationProvider.provideRotation();
    } catch (IllegalStateException e) {
      rotation = new RandomRotationProvider(this.mapManager, this.matchFactory).provideRotation();
    }
    this.matchManager = new MatchManager(this.matchFactory, rotation);

    PlayerSettings.register(DeathMessage.SETTING);
    PlayerSettings.register(VoteModule.SETTING);


    try {
      this.matchManager.start();
    } catch (IOException e) {
      this.getLogger().log(Level.SEVERE, "Could not start rotation", e);
      this.getPluginLoader().disablePlugin(this);
      return;
    }

    // Fun
    // new Friday13(this);

    RestartCommands.HANDLER = () -> get().getMatchManager().getRotation()
        .queueRestart();
    AtlasTask.of(() -> ready.set(true)).later(40);

    BlockPropStore.initBlocks();
    BlockPropStore.initTools();
  }

  @Override
  public void onDisable() {
    if (this.matchManager != null) {
      this.matchManager.shutdown();
    }
    if (this.componentManager != null) {
      this.componentManager.disable();
    }
    if (this.loader != null) {
      this.loader.disableAll();
    }
  }

  private void registerComponents() {
    this.componentManager = new ComponentManager(Bukkit.getPluginManager(), this, registrar);
    this.componentManager.register(TabListComponent.class);
    this.componentManager.register(DebuggingComponent.class);
    this.componentManager.register(MapNotificationComponent.class);
    this.componentManager.register(SidebarComponent.class);
    this.componentManager.register(VisualEffectComponent.class);
    this.componentManager.register(SoundComponent.class);
    this.componentManager.register(ArrowRemovalComponent.class);
    this.sideBar = this.componentManager.get(SidebarComponent.class);
    this.componentManager.register(MessageCommands.class);
    this.componentManager.register(Premium.class);
    this.componentManager.register(StaffChannels.class);

    this.componentManager.register(FreezeComponent.class);
  }

  private void registerListeners() {
    Events.register(new BlockChangeListener());
    Events.register(new EntityChangeListener());
    Events.register(new AtlasListener());
    Events.register(new AFKKickTask().start());
    Bukkit.getPluginManager().registerEvents(new LockingSharingListener(), this);
  }

  private void registerCommands(AvicusCommandsRegistration registrar) {
    registrar.register(ChannelCommands.class);
    registrar.register(Parent.class);
    registrar.register(JoinCommands.class);
    registrar.register(RotationCommands.class);
    registrar.register(StateCommands.class);
    registrar.register(GameCommands.class);
    registrar.register(ResourcePackCommand.class);
    registrar.register(GroupParentCommand.class);
    registrar.register(GenericCommands.class);

    // Modular
    registrar.register(KitCommands.class);
    registrar.register(ObserverCommands.class);
    registrar.register(VoteCommands.class);
    registrar.register(ParentCommand.class);

    try {
      Class.forName("com.sk89q.worldedit.WorldEdit");
      registrar.register(WorldEditCommands.class);
    } catch (ClassNotFoundException ignored) {
    }

    registrar.register(RestartCommands.class);
    registrar.register(AnnounceCommands.Parent.class);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    try {
      this.commandManager.execute(cmd.getName(), args, sender, sender);
    } catch (AbstractTranslatableCommandException e) {
      sender.sendMessage(AbstractTranslatableCommandException.format(e));
    } catch (CommandNumberFormatException e) {
      sender.sendMessage(AbstractTranslatableCommandException
          .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_NUMBER_EXPECTED,
              new UnlocalizedText(e.getActualText())));
    } catch (CommandPermissionsException e) {
      sender.sendMessage(AbstractTranslatableCommandException
          .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_NO_PERMISSION));
    } catch (CommandUsageException e) {
      sender.sendMessage(AbstractTranslatableCommandException
          .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_INVALID_USAGE,
              new UnlocalizedText(e.getUsage())));
    } catch (CommandMatchException e) {
      sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERROR_MATCH_MISSING));
    } catch (CommandException e) {
      sender.sendMessage(AbstractTranslatableCommandException
          .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_INTERNAL_ERROR));
      e.printStackTrace();
    }

    return true;
  }
}
