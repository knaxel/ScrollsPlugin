/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scrolls.configuration;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.EntityType;
import scrolls.ScrollsPlugin;

/**
 *
 * @author knaxel
 */
public class SpawnConfig extends Config {

    private final List<EntityType> scrollDroppers;
    private double scroll, darkscroll, cleanslatescroll, chaosscroll;

    public SpawnConfig(ScrollsPlugin plugin) {
        super(plugin, "spawn_configuration");
        scrollDroppers = new ArrayList<>();
        reload();
    }

    @Override
    void setDefaults() {
        config.set("droprate.scroll", .05);
        config.set("droprate.darkscroll", .03);
        config.set("droprate.cleanslatescroll", .01);
        config.set("droprate.chaosscroll", .005);
        scrollDroppers.add(0,EntityType.CAVE_SPIDER);
        scrollDroppers.add(1,EntityType.SPIDER);
        scrollDroppers.add(2,EntityType.ZOMBIE);
        scrollDroppers.add(3,EntityType.ZOMBIE_HORSE);
        scrollDroppers.add(4,EntityType.ZOMBIE_VILLAGER);
        scrollDroppers.add(5,EntityType.ENDERMAN);
        scrollDroppers.add(6,EntityType.CREEPER);
        scrollDroppers.add(7,EntityType.HUSK);
        scrollDroppers.add(8,EntityType.GHAST);
        scrollDroppers.add(9,EntityType.SKELETON_HORSE);
        scrollDroppers.add(10,EntityType.SKELETON);
        scrollDroppers.add(11,EntityType.GUARDIAN);
        scrollDroppers.add(12,EntityType.SHULKER);
        scrollDroppers.add(13,EntityType.MAGMA_CUBE);
        scrollDroppers.add(14,EntityType.WITCH);
        scrollDroppers.add(15,EntityType.SILVERFISH);
        scrollDroppers.add(16,EntityType.SLIME);
        scrollDroppers.add(17,EntityType.STRAY);
        scrollDroppers.add(18,EntityType.VEX);
        scrollDroppers.add(19,EntityType.VINDICATOR);
        scrollDroppers.add(20,EntityType.ENDERMITE);
        scrollDroppers.add(21,EntityType.EVOKER);
        scrollDroppers.add(22,EntityType.ELDER_GUARDIAN);
        scrollDroppers.add(23,EntityType.WITHER_SKELETON);
        for (EntityType type : scrollDroppers) {
            if (!type.isAlive()) {
                scrollDroppers.remove(type);
                continue;
            }
            List<String> array = new ArrayList<>();
            for (int i = 0; i < scrollDroppers.size(); i++) {
                array.add(i,scrollDroppers.get(i).toString());
            }
            config.set("entity_list", array);
        }
    }

    @Override
    void loadToPlugin() {
        scroll = config.getDouble("droprate.scroll");
        darkscroll = config.getDouble("droprate.darkscroll");
        cleanslatescroll = config.getDouble("droprate.cleanslatescroll");
        chaosscroll = config.getDouble("droprate.chaosscroll");
        
        List<String> list = config.getStringList("entity_list");
        ScrollsPlugin.lg("Scroll Droppers: " + list.toString());
        config.getStringList("entity_list").stream().map((name) -> EntityType.valueOf(name)).forEachOrdered((type) -> {
            scrollDroppers.add(type);
        });
        
    }
    public double getScrollRate() {
        return scroll;
    }
    public double getDarkScrollRate() {
        return darkscroll;
    }

    public double getChaosScrollRate() {
        return chaosscroll;
    }

    public double getCleanslateScrollRate() {
        return cleanslatescroll;
    }

    public boolean canDrop(EntityType type) {
        return scrollDroppers.contains(type);
    }

}
