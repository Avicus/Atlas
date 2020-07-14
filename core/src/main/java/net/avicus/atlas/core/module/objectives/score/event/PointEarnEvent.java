package net.avicus.atlas.core.module.objectives.score.event;

import javax.annotation.Nullable;
import lombok.Getter;
import net.avicus.atlas.core.event.objective.ObjectiveStateChangeEvent;
import net.avicus.atlas.core.module.objectives.score.ScoreObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PointEarnEvent extends ObjectiveStateChangeEvent {

  private static final HandlerList handlers = new HandlerList();
  @Getter
  @Nullable
  private final Player player;
  @Getter
  private final int amount;

  public PointEarnEvent(ScoreObjective objective, @Nullable Player player, int amount) {
    super(objective);
    this.player = player;
    this.amount = amount;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
