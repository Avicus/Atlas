package net.avicus.atlas.core.game;

import java.util.List;
import net.avicus.atlas.core.game.author.Author;
import net.avicus.atlas.core.util.Version;

public interface MinecraftMap {

  Version getSpecification();

  String getSlug();

  String getName();

  Version getVersion();

  List<Author> getAuthors();

  List<Author> getContributors();
}
