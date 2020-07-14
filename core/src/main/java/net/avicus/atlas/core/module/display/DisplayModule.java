package net.avicus.atlas.core.module.display;

import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.event.match.MatchCloseEvent;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.Module;
import net.avicus.atlas.core.util.Events;
import org.bukkit.event.EventHandler;

@ToString
public class DisplayModule implements Module {

  @Getter
  private final ScoreboardHandler scoreboard;
  private final FriendlyInvisibilityTask friendlyInvisTask;

  public DisplayModule(Match match) {
    this.scoreboard = new ScoreboardHandler(match);
    this.friendlyInvisTask = new FriendlyInvisibilityTask(match, this);
  }

  @Override
  public void open() {
    Events.register(this.scoreboard);
    this.friendlyInvisTask.start();
    Events.register(this.friendlyInvisTask);
  }

  @EventHandler
  public void onMatchClose(MatchCloseEvent event) {
    this.friendlyInvisTask.cancel0();
    Events.unregister(this.friendlyInvisTask);
  }
}
