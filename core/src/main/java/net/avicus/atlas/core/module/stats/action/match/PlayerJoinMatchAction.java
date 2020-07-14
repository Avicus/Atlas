package net.avicus.atlas.core.module.stats.action.match;

import java.time.Instant;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.states.State;
import org.bukkit.entity.Player;

@ToString(callSuper = true)
public class PlayerJoinMatchAction extends PlayerMatchAction {

  public PlayerJoinMatchAction(Player actor, Instant when, Match match, State matchState) {
    super(actor, when, match, matchState);
  }

  @Override
  public double getScore() {
    return 0;
  }

  @Override
  public String getDebugMessage() {
    return "Match Join";
  }
}
