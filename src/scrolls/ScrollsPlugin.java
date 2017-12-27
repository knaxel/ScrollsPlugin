
package scrolls;

import scrolls.event.ScrollInventoryListener;
import java.util.logging.Level;
import scrolls.commands.ScrollsCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import scrolls.configuration.ScrollsConfig;
import scrolls.configuration.SpawnConfig;
import scrolls.event.ScrollSpawnListener;

/**
 *
 * @author knaxel
 */
public class ScrollsPlugin extends JavaPlugin {

    private ScrollFactory scrollFactory;
    private ScrollsConfig config;
    private SpawnConfig spawnConfig;

    @Override
    public void onEnable() {
        ScrollsPlugin.lg("Hello, thank you for trying the scrolls plugin!");
        ScrollsPlugin.lg("Loading configuration files");
        config = new ScrollsConfig(this);
        spawnConfig = new SpawnConfig(this);
        ScrollsPlugin.lg("Creating scroll factory...");
        scrollFactory = new ScrollFactory(config);
        ScrollsPlugin.lg("Setting command executors...");
        this.getCommand("scrolls").setExecutor(new ScrollsCommand(this));
        ScrollsPlugin.lg("Registering events...");
        this.getServer().getPluginManager().registerEvents(new ScrollInventoryListener(scrollFactory), this);
        this.getServer().getPluginManager().registerEvents(new ScrollSpawnListener(scrollFactory, spawnConfig), this);
    }

    @Override
    public void onDisable() {
        ScrollsPlugin.lg("Goodbye and thank you! from the ScrollsPlugin!");
    }

    public static void lg(String arg) {
        Bukkit.getLogger().log(Level.INFO, arg);
    }

    public ScrollFactory getScrollFactory() {
        return this.scrollFactory;
    }

    public void reload() {
        ScrollsPlugin.lg("Reloading configurations...");
        config.reload();
        spawnConfig.reload();
        ScrollsPlugin.lg("Setting command executors...");
    }

}
