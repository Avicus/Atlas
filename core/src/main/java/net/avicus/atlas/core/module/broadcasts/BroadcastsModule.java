package net.avicus.atlas.core.module.broadcasts;

import java.util.List;
import java.util.stream.Collectors;
import net.avicus.atlas.core.match.Match;
import net.avicus.atlas.core.module.Module;

/**
 * This module handles the periodic display of match-specific messages players.
 * <p>
 * Must be loaded after checks.
 */
public class BroadcastsModule implements Module {

  private final List<BroadcastTask> tasks;

  public BroadcastsModule(Match match, List<Broadcast> broadcasts) {
    this.tasks = broadcasts.stream().map((broadcast) -> broadcast.createTask(match))
        .collect(Collectors.toList());
  }

  @Override
  public void open() {
    this.tasks.forEach(BroadcastTask::start);
  }

  @Override
  public void close() {
    this.tasks.forEach(BroadcastTask::cancel0);
  }
}