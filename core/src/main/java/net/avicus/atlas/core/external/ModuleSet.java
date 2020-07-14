package net.avicus.atlas.core.external;

import java.util.logging.Logger;
import net.avicus.atlas.core.Atlas;
import net.avicus.atlas.core.component.ComponentManager;
import net.avicus.atlas.core.match.MatchFactory;
import net.avicus.compendium.commands.AvicusCommandsRegistration;

/**
 * Class to represent a jar that is loaded externally.
 **/
public abstract class ModuleSet {

  public abstract void onEnable();

  public abstract void onDisable();

  public void setAtlas(Atlas atlas) {
  }

  public void setMatchFactory(MatchFactory matchFactory) {
  }

  public void setLogger(Logger logger) {
  }

  public void setRegistrar(AvicusCommandsRegistration registrar) {
  }

  public void onComponentsEnable(ComponentManager componentManager) {
  }
}
