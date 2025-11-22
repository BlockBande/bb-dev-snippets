package net.blockbande.misc.snow;

import lombok.Getter;
import net.blockbande.game.base.CoreModule;
import net.blockbande.game.base.GameService;
import net.blockbande.modularity.core.bukkit.BukkitModule;
import net.blockbande.modularity.core.bukkit.ModularityBukkit;
import net.blockbande.modularity.core.module.ModuleDescription;
import net.blockbande.protocol.game.base.server.WorldType;

import java.util.Set;

@Getter
@ModuleDescription(
    name = "snow",
    version = "1.0",
    dependencies = { "core" }
)
public class SnowModule extends BukkitModule {

  private static final Set<WorldType> SNOW_WORLDS =
      Set.of(WorldType.SPAWN, WorldType.MALL, WorldType.PLOT);

  private boolean snowEnabled;

  @Override
  public void onLoad() {
    CoreModule.getInstance()
        .getGuiceModules()
        .add(binder -> binder.bind(SnowModule.class).toInstance(this));
  }

  @Override
  public void onEnable() {
    var coreModule = CoreModule.getInstance();
    var injector = coreModule.getInjector();
    var plugin = ModularityBukkit.getInstance();

    CoreModule.getInstance()
        .registerListeners(getLogger(), "net.blockbande.misc.snow.listener");
    CoreModule.getInstance()
        .registerCommands(this.getLogger(), "net.blockbande.misc.snow.command");

    var gameService = injector.getInstance(GameService.class);
    if (SNOW_WORLDS.contains(gameService.getType())) {
      this.snowEnabled = true;
      injector
          .getInstance(FlakesRunnable.class)
          .runTaskTimer(plugin, 0L, 2L);
    }
  }
}