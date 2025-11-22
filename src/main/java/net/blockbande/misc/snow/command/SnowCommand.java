package net.blockbande.misc.snow.command;

import com.google.inject.Inject;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import net.blockbande.game.base.user.GameUserRepository;
import net.blockbande.misc.snow.SnowAttachment;
import net.blockbande.modularity.command.Command;
import net.blockbande.modularity.command.CommandAnnotation;
import org.bukkit.entity.Player;

@CommandAnnotation(name = "schnee", cooldown = 2000)
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SnowCommand extends Command {

  private final GameUserRepository userRepository;

  @Override
  public void run(Player player, String[] args) {
    var user = userRepository.getUser(player);
    var snowAttachment = user.getAttachment(SnowAttachment.class);

    if (snowAttachment == null) {
      user.sendMessage("snow-not-available");
      return;
    }

    if (args.length == 0) {
      user.sendMessage("snow-command-usage");
      return;
    }

    String option = args[0].toLowerCase(Locale.ROOT);

    switch (option) {
      case "off" -> {
        snowAttachment.setSnowEnabled(false);
        user.sendMessage("snow-disabled-command");
      }
      case "on" -> {
        snowAttachment.setSnowEnabled(true);
        user.sendMessage("snow-enabled-command");
      }
      default -> user.sendMessage("snow-command-usage");
    }
  }
}