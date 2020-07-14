package net.avicus.atlas.core.component.dev;

import java.text.MessageFormat;
import java.util.logging.Level;
import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.component.ListenerComponent;
import net.avicus.atlas.core.event.match.MatchStateChangeEvent;
import org.bukkit.event.EventHandler;

public class DebuggingComponent implements ListenerComponent {

  private void debug(String prefix, String message, Object... vars) {
    Atlas.get().getLogger()
        .log(Level.INFO, MessageFormat.format("[" + prefix + "] " + message, vars) + "");
  }

  @Override
  public void enable() {

  }

  @EventHandler
  public void onMatchStateChange(MatchStateChangeEvent event) {
    debug("State", "{0} -> {1}", event.getFrom(), event.getTo());
  }
}
