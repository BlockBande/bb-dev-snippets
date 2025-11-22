package net.blockbande.misc.snow;

import com.google.common.util.concurrent.Futures;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.blockbande.api.utilities.future.FutureSuccessCallback;
import net.blockbande.game.base.GameService;
import net.blockbande.game.base.user.attachment.Attachment;
import net.blockbande.modularity.connection.UserServiceHelper;
import net.blockbande.modularity.core.bukkit.SpigotSyncExecutor;
import net.blockbande.protocol.game.base.server.WorldType;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SnowAttachment extends Attachment {

  private static final String SETTING_KEY = "citybuild-snow";
  private static final String ON_VALUE = "ON";

  private final SnowModule snowModule;
  private final GameService gameService;

  private boolean snowEnabled;

  @Override
  public void init() {
    Player player = user.getPlayer();
    if (player == null) {
      return;
    }

    var future = UserServiceHelper.getUserSetting(player.getUniqueId(), SETTING_KEY);

    FutureSuccessCallback<String> callback = setting -> {
      boolean defaultEnabled = !player.getName().startsWith("!");
      boolean isOn = ON_VALUE.equals(setting);

      this.snowEnabled = setting == null ? defaultEnabled : isOn;

      if (gameService.getType() == WorldType.SPAWN) {
        user.sendMessage(snowEnabled ? "snow-enabled" : "snow-disabled");
      }
    };

    Futures.addCallback(future, callback, SpigotSyncExecutor.getExecutor());
  }

  public void setSnowEnabled(boolean snowEnabled) {
    if (this.snowEnabled == snowEnabled) {
      return;
    }

    this.snowEnabled = snowEnabled;

    Player player = user.getPlayer();
    if (player != null) {
      UserServiceHelper.setUserSetting(player.getUniqueId(), SETTING_KEY, snowEnabled);
    }
  }
}