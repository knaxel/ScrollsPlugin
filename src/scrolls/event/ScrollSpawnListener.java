
package scrolls.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import scrolls.ScrollFactory;
import scrolls.configuration.SpawnConfig;

/**
 *
 * @author knaxel
 */
public class ScrollSpawnListener implements Listener {

    private final SpawnConfig spawnConfig;
    private final ScrollFactory scrollFactory;

    public ScrollSpawnListener(ScrollFactory factory, SpawnConfig spawnConfig) {
        scrollFactory = factory;
        this.spawnConfig = spawnConfig;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) {
            return;
        }
        if (!e.getEntity().getKiller().hasPermission("scrolls.use")) {
            return;
        }
        if (spawnConfig.canDrop(e.getEntityType())) {
            if (Math.random() < spawnConfig.getScrollRate()) {
                e.getDrops().add(scrollFactory.getRandomEnchantmentScroll());
            }
            if (Math.random() < spawnConfig.getDarkScrollRate()) {
                e.getDrops().add(scrollFactory.getRandomDarkEnchantmentScroll());
            }
            if (Math.random() < spawnConfig.getCleanslateScrollRate()) {
                e.getDrops().add(scrollFactory.getRandomCleanSlateScroll());
            }
            if (Math.random() < spawnConfig.getChaosScrollRate()) {
                e.getDrops().add(scrollFactory.getRandomChaosScroll());
            }
        }
    }

}
