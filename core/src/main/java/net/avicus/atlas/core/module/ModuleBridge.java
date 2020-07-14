package net.avicus.atlas.core.module;

public interface ModuleBridge<T extends Module> {

  void onOpen(T module);

  void onClose(T module);
}
