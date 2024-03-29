package net.avicus.atlas.core.component.visual;

import static net.avicus.atlas.core.util.ObjectiveUtils.CHECK_MARK;
import static net.avicus.atlas.core.util.ObjectiveUtils.SEPARATOR;
import static net.avicus.atlas.core.util.ObjectiveUtils.X_MARK;
import static net.avicus.atlas.core.util.ObjectiveUtils.getDistanceDisplay;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Setter;
import net.avicus.atlas.event.RefreshUIEvent;
import net.avicus.atlas.core.NetworkIdentification;
import net.avicus.atlas.core.component.ListenerComponent;
import net.avicus.atlas.core.event.group.GroupMaxPlayerCountChangeEvent;
import net.avicus.atlas.core.event.group.GroupRenameEvent;
import net.avicus.atlas.core.event.group.PlayerChangedGroupEvent;
import net.avicus.atlas.core.event.match.MatchOpenEvent;
import net.avicus.atlas.core.event.player.PlayerSpawnCompleteEvent;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.groups.Competitor;
import net.avicus.atlas.core.module.groups.GroupsModule;
import net.avicus.atlas.core.module.groups.teams.Team;
import net.avicus.atlas.core.module.objectives.Objective;
import net.avicus.atlas.core.module.objectives.ObjectivesModule;
import net.avicus.atlas.core.module.objectives.entity.EntityObjective;
import net.avicus.atlas.core.module.objectives.lcs.LastCompetitorStanding;
import net.avicus.atlas.core.module.objectives.locatable.LocatableUpdateDistanceEvent;
import net.avicus.atlas.core.module.objectives.lts.LastTeamStanding;
import net.avicus.atlas.core.module.objectives.score.ScoreObjective;
import net.avicus.atlas.core.module.objectives.score.event.PointEarnEvent;
import net.avicus.atlas.core.module.states.StatesModule;
import net.avicus.atlas.core.util.AtlasTask;
import net.avicus.atlas.core.util.Events;
import net.avicus.atlas.core.util.Messages;
import net.avicus.atlas.core.util.ObjectiveRenderer;
import net.avicus.atlas.core.util.Sidebar;
import net.avicus.compendium.StringUtil;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizableFormat;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.utils.Strings;
import net.avicus.grave.event.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SidebarComponent implements ListenerComponent {

  public static Set<SidebarHook> HOOKS = Sets.newHashSet();
  public static ObjectiveRenderer DEFAULT_RENDERER = new ObjectiveRenderer() {
    @Override
    public String getDisplay(Match match, Competitor competitor, Player viewer, Objective objective,
        boolean showName) {
      StringBuilder result = new StringBuilder();
      Locale locale = viewer.getLocale();

      if (objective instanceof LastTeamStanding) {
        LastTeamStanding standing = (LastTeamStanding) objective;
        Team team = standing.getTeam();
        StatesModule module = match.getRequiredModule(StatesModule.class);

        int members = team.getMembers().size();

        LocalizedNumber number = new LocalizedNumber(members, TextStyle.ofColor(ChatColor.GOLD));
        LocalizableFormat format =
            members == 1 ? Messages.UI_NUM_PLAYERS : Messages.UI_NUM_PLAYERS_PLURAL;
        if (module.isPlaying()) {
          format =
              members == 1 ? Messages.UI_PLAYERS_REMAINING : Messages.UI_PLAYERS_REMAINING_PLURAL;
        }
        result.append(format.with(ChatColor.GRAY, number).translate(locale).toLegacyText());
      } else if (objective instanceof LastCompetitorStanding) {
        LastCompetitorStanding standing = (LastCompetitorStanding) objective;
        StatesModule module = match.getRequiredModule(StatesModule.class);

        int remaining = standing.currentCompetitors().size();

        LocalizedNumber number = new LocalizedNumber(remaining, TextStyle.ofColor(ChatColor.GOLD));
        LocalizableFormat format =
            remaining == 1 ? Messages.UI_NUM_PLAYERS : Messages.UI_NUM_PLAYERS_PLURAL;
        if (module.isPlaying()) {
          format =
              remaining == 1 ? Messages.UI_PLAYERS_REMAINING : Messages.UI_PLAYERS_REMAINING_PLURAL;
        }

        result.append(format.with(ChatColor.GRAY, number).translate(locale).toLegacyText());
      } else if (objective instanceof EntityObjective) {
        EntityObjective entity = (EntityObjective) objective;

        if (entity.isCompleted()) {
          result.append(ChatColor.GREEN);
          result.append(CHECK_MARK);
        } else if (entity.getHighestCompleter().isPresent()) {
          result.append(Strings.toChatColor(entity.getCompletion()));
          result.append(StringUtil.toPercent(entity.getCompletion()));
        } else {
          result.append(ChatColor.RED);
          result.append(X_MARK);
        }

        result.append(getDistanceDisplay(competitor, viewer, objective, !showName));

        if (showName) {
          result.append(SEPARATOR);
          // No owner, has a highest completer
          Optional<Competitor> highest = entity.getHighestCompleter();
          if (!entity.getOwner().isPresent() && highest.isPresent()) {
            result.append(highest.get().getChatColor());
          }
          result.append(entity.getName().translate(locale));
        }
      } else if (objective instanceof ScoreObjective) {
        ScoreObjective score = (ScoreObjective) objective;

        if (score.getTeam().isPresent()) {
          result.append(score.getPoints(score.getTeam().get()));
          result.append(ChatColor.GRAY);
          if (score.getLimit().isPresent()) {
            result.append("/");
            result.append(score.getLimit().get());
          }
          if (showName) {
            result.append(" ");
            result.append(ChatColor.WHITE);
            result.append(score.getName().translate(viewer));
          } else {
            result.append("   ");
          }
        } else {
          result.append("Bad objective.");
        }
      }
      return result.toString();
    }
  };
  @Setter
  private Match match;
  private Map<Player, Sidebar> sidebars;
  private GroupsModule groups;
  private ObjectivesModule module;

  @Override
  public void enable() {
    this.sidebars = new HashMap<>();
    syncUpdate();
    HOOKS.forEach(h -> h.setComponent(this));
    HOOKS.forEach(Events::register);
  }

  @Override
  public void disable() {
    HOOKS.forEach(Events::unregister);
  }

  public void syncUpdate() {
    new AtlasTask() {
      @Override
      public void run() {
        update();
      }
    }.now();
  }

  public void syncUpdate(Player player) {
    new AtlasTask() {
      @Override
      public void run() {
        update(player);
      }
    }.now();
  }

  public void update() {
    Bukkit.getOnlinePlayers()
        .forEach(this::update);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onMatchOpen(MatchOpenEvent event) {
    setMatch(event.getMatch());
    HOOKS.forEach(h -> h.setMatch(event.getMatch()));
    this.groups = this.match.getRequiredModule(GroupsModule.class);
    this.module = this.match.getRequiredModule(ObjectivesModule.class);
    this.sidebars.clear();
    Bukkit.getOnlinePlayers()
        .forEach((player) -> {
          AtlasTask.of(() -> this.syncUpdate(player)).nowAsync();
        });
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.sidebars.remove(event.getPlayer());
    syncUpdate();
  }

  public Sidebar getSidebar(Player player) {
    Sidebar sidebar = this.sidebars.get(player);
    if (sidebar == null) {
      sidebar = new Sidebar(player.getScoreboard());
      this.sidebars.put(player, sidebar);
      sidebar.addURL();

      // Title
      Localizable title = new UnlocalizedText(NetworkIdentification.NAME);
      for (SidebarHook hook : HOOKS) {
        Localizable opt = hook.getTitleFinal(module);
        if (opt != null) {
          title = opt;
        }
      }

      title.style().color(ChatColor.AQUA);
      sidebar.setTitle(title.translate(player.getLocale()).toLegacyText());
    }
    return sidebar;
  }

  public void update(Player player) {
    Sidebar sidebar = getSidebar(player);

    List<String> rows = new ArrayList<>();

    HOOKS.forEach(h -> rows.addAll(h.getRows(player, groups, sidebar, module)));

    for (int i = 1; i <= Sidebar.Constants.MAX_ROWS; i++) {
      if (i <= rows.size()) {
        sidebar.setRow(rows.size(), i, rows.get(i - 1));
      } else {
        sidebar.setRow(rows.size(), i, null);
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    // delayed by 1 second for locale to be sent
    new AtlasTask() {
      @Override
      public void run() {
        if (event.getPlayer().isOnline()) {
          update(event.getPlayer());
        }
      }
    }.later(20);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onDeath(final PlayerDeathEvent event) {
    this.delayedUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onSpawn(final PlayerSpawnCompleteEvent event) {
    this.delayedUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onChangeTeam(PlayerChangedGroupEvent event) {
    syncUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onGroupRename(GroupRenameEvent event) {
    syncUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void groupMaxPlayersChange(final GroupMaxPlayerCountChangeEvent event) {
    this.syncUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void distanceChange(final LocatableUpdateDistanceEvent event) {
    this.delayedUpdate();
  }


  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void pointEarn(final PointEarnEvent event) {
    this.delayedUpdate();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onRefresh(final RefreshUIEvent event) {
    this.delayedUpdate();
  }

  public void delayedUpdate() {
    new AtlasTask() {
      @Override
      public void run() {
        SidebarComponent.this.update();
      }
    }.later(3); // don't change
  }
}
