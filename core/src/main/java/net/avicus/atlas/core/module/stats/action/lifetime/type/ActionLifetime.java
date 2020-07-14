package net.avicus.atlas.core.module.stats.action.lifetime.type;

import com.google.common.collect.Lists;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.core.module.stats.action.base.Action;

@ToString
public abstract class ActionLifetime<T extends Action> {

  @Getter
  private final Instant start;
  private final List<T> actions = Lists.newArrayList();
  @Getter
  private Instant end;

  public ActionLifetime(Instant start) {
    this.start = start;
  }

  public void end() {
    this.end = Instant.now();
  }

  public
  @Nonnull
  List<T> getActions() {
    return Collections.unmodifiableList(this.actions);
  }

  public void addAction(T action) {
    this.actions.add(action);
  }
}
