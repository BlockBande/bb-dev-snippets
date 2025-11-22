package net.blockbande.misc.snow;

import com.google.inject.Inject;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import net.blockbande.game.base.user.GameUser;
import net.blockbande.game.base.user.GameUserRepository;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class FlakesRunnable extends BukkitRunnable {

  private static final double RADIUS = 10.0;
  private static final int AMOUNT = 50;

  private final GameUserRepository userRepository;

  @Override
  public void run() {
    List<GameUser> users =
        userRepository.getUsers().stream()
            .filter(user -> {
              var attachment = user.getAttachment(SnowAttachment.class);
              return attachment != null && attachment.isSnowEnabled();
            })
            .toList();

    if (users.isEmpty()) {
      return;
    }

    var random = ThreadLocalRandom.current();

    for (int i = 0; i < AMOUNT; i++) {
      double xAdditive = (random.nextDouble() - 0.5D) * RADIUS * 2.0D;
      double max = Math.sqrt(RADIUS * RADIUS - xAdditive * xAdditive) * 2.0D;
      double yAdditive = (random.nextDouble() - 0.5D) * max;
      double zAdditive = (random.nextDouble() - 0.5D) * max;

      for (GameUser user : users) {
        Player player = user.getPlayer();
        if (player == null) {
          continue;
        }

        Location playerLoc = player.getLocation();
        Location loc = playerLoc.clone().add(xAdditive, yAdditive, zAdditive);

        if (loc.getWorld().getHighestBlockYAt(loc) < loc.getY()) {
          sendParticles(player, loc.getX(), loc.getY(), loc.getZ(), 0.0D, 10);
        }
      }
    }
  }

  private void sendParticles(Player player, double x, double y, double z, double data, int amount) {
    player.spawnParticle(Particle.FIREWORK, x, y, z, amount, 0, 0, 0, data);
  }
}