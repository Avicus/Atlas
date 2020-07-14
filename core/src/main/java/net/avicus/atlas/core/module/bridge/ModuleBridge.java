package net.avicus.atlas.core.module.bridge;

import net.avicus.atlas.core.module.Module;

public abstract class ModuleBridge<T extends Module> {

  public abstract void onInitialize(T module);

  public abstract void onOpen(T module);

  public abstract void onClose(T module);
}
