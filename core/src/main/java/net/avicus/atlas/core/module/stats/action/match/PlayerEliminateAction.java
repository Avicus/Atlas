package net.avicus.atlas.core.module.stats.action.match;

import java.time.Instant;
import lombok.ToString;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.states.State;
import org.bukkit.entity.Player;

@ToString(callSuper = true)
public class PlayerEliminateAction extends PlayerLeaveMatchAction {

  public PlayerEliminateAction(Player actor, Instant when, Match match, State matchState) {
    super(actor, when, match, matchState);
  }
}
