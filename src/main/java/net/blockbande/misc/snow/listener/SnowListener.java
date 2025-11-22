package net.blockbande.misc.snow.listener;

import net.blockbande.game.base.event.GameUserJoinEvent;
import net.blockbande.misc.snow.SnowAttachment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SnowListener implements Listener {

  @EventHandler
  public void onGameUserJoin(GameUserJoinEvent event) {
    event.getUser().attach(SnowAttachment.class);
  }
}
