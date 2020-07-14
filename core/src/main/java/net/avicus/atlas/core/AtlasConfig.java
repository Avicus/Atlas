package net.avicus.atlas.core;

import java.util.List;
import lombok.Getter;
import net.avicus.compendium.config.Config;
import net.avicus.compendium.config.inject.ConfigKey;
import net.avicus.compendium.config.inject.ConfigPath;
import org.joda.time.Duration;

public class AtlasConfig {

  @Getter
  @ConfigKey(key = "locales-path")
  public static String localesPath;

  @Getter
  @ConfigKey
  private static List<Config> libraries;

  @Getter
  @ConfigKey(key = "max-group-imbalance")
  private static double maxGroupImbalance;

  @Getter
  @ConfigPath("rotation")
  @ConfigKey(key = "path")
  private static String rotationFile;

  @Getter
  @ConfigPath("rotation")
  @ConfigKey(key = "restart-on-end")
  private static boolean rotationRestart;

  @Getter
  @ConfigPath("rotation")
  @ConfigKey(key = "randomize")
  private static boolean rotationRandomize;

  @Getter
  @ConfigPath("rotation")
  @ConfigKey(key = "auto-start")
  private static boolean rotationAutoStart;

  @Getter
  @ConfigKey(key = "delete-matches")
  private static boolean deleteMatches;

  @Getter
  @ConfigKey(key = "scrimmage")
  private static boolean scrimmage;

  @Getter
  @ConfigPath("github")
  @ConfigKey(key = "auth")
  private static boolean githubAuth;

  @Getter
  @ConfigPath("github")
  @ConfigKey(key = "username")
  private static String githubUsername;

  @Getter
  @ConfigPath("github")
  @ConfigKey(key = "token")
  private static String githubToken;

  @ConfigPath("website")
  public static class Website {

    @ConfigKey(key = "base")
    @Getter
    private static String base;

    @ConfigKey(key = "map")
    @Getter
    private static String map;

    public static String resolvePath(String slug) {
      return base + map.replace("{0}", slug);
    }
  }

  @Getter
  @ConfigKey(key = "send-deprecation-warnings")
  private static boolean sendDeprecationWarnings;

  @ConfigPath("chat")
  public static class Chat {

    @Getter
    @ConfigKey(key = "strip-color")
    private static boolean stripColor;
  }

  @ConfigPath("channels")
  public static final class Channel {

    @Getter
    @ConfigKey
    public static boolean enabled;

    @ConfigPath("staff")
    public static final class Staff {

      @Getter
      @ConfigKey
      public static boolean enabled;
    }

    @ConfigPath("reports")
    public static final class Report {

      @ConfigKey
      private static long cooldown;
      @Getter
      @ConfigKey
      private static boolean enabled;

      public static Duration getCooldown() {
        return Duration.standardSeconds(cooldown);
      }
    }
  }

  @ConfigPath("freeze")
  public static final class Freeze {

    @Getter
    @ConfigKey
    private static boolean enabled;

    @ConfigPath("radius")
    public static final class Radius {

      @Getter
      @ConfigKey
      private static int extinguish;

      @Getter
      @ConfigKey
      private static int tnt;
    }
  }
}
