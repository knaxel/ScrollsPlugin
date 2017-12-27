
package scrolls.configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import scrolls.ScrollsPlugin;

/**
 *
 * @author knaxel
 */
public class ScrollsConfig extends Config {

    private boolean flipRateToEnchantment; // make enchantments and level correlated to higher success rates
    private boolean trackScrollData; // track number of which scrolls are made and how
    private double expoSuccessRates; //curve determinant for success rates
    private double expoEnchantments; //curve determinant for echantments
    private double expoLevel; //curve determinant for level
    private int scrollSlots;
    private final Map<ScrollDataType, Object> scrolls;
    private final Map<ScrollDataType, Object> darkScrolls;
    private final Map<ScrollDataType, Object> cleanSlateScrolls;
    private final Map<ScrollDataType, Object> chaosScrolls;
    private final Map<ScrollDataType, Object> safetyScrolls;
    private final Enchantment[] enchantmentRarity;

    public ScrollsConfig(ScrollsPlugin plugin) {
        super(plugin, "scrolls_configuration");
        scrolls = new EnumMap<>(ScrollDataType.class);
        darkScrolls = new EnumMap<>(ScrollDataType.class);
        cleanSlateScrolls = new EnumMap<>(ScrollDataType.class);
        chaosScrolls = new EnumMap<>(ScrollDataType.class);
        safetyScrolls = new EnumMap<>(ScrollDataType.class);
        enchantmentRarity = new Enchantment[30];
        reload();
    }

    @Override
    void setDefaults() {
        String[] ench = new String[30];
        ench[0] = Enchantment.DAMAGE_ALL.getName();
        ench[1] = Enchantment.SILK_TOUCH.getName();
        ench[2] = Enchantment.LOOT_BONUS_BLOCKS.getName();
        ench[3] = Enchantment.LOOT_BONUS_MOBS.getName();
        ench[4] = Enchantment.DURABILITY.getName();
        ench[5] = Enchantment.SWEEPING_EDGE.getName();
        ench[6] = Enchantment.KNOCKBACK.getName();
        ench[7] = Enchantment.FIRE_ASPECT.getName();
        ench[8] = Enchantment.MENDING.getName();
        ench[9] = Enchantment.THORNS.getName();
        ench[10] = Enchantment.ARROW_INFINITE.getName();
        ench[11] = Enchantment.ARROW_DAMAGE.getName();
        ench[12] = Enchantment.ARROW_KNOCKBACK.getName();
        ench[13] = Enchantment.ARROW_FIRE.getName();
        ench[14] = Enchantment.DAMAGE_UNDEAD.getName();
        ench[15] = Enchantment.DAMAGE_ARTHROPODS.getName();
        ench[16] = Enchantment.DIG_SPEED.getName();
        ench[17] = Enchantment.OXYGEN.getName();
        ench[18] = Enchantment.LUCK.getName();
        ench[19] = Enchantment.DEPTH_STRIDER.getName();
        ench[20] = Enchantment.WATER_WORKER.getName();
        ench[21] = Enchantment.FROST_WALKER.getName();
        ench[22] = Enchantment.LURE.getName();
        ench[23] = Enchantment.PROTECTION_ENVIRONMENTAL.getName();
        ench[24] = Enchantment.PROTECTION_FIRE.getName();
        ench[25] = Enchantment.PROTECTION_FALL.getName();
        ench[26] = Enchantment.PROTECTION_EXPLOSIONS.getName();
        ench[27] = Enchantment.PROTECTION_PROJECTILE.getName();
        ench[28] = Enchantment.VANISHING_CURSE.getName();
        ench[29] = Enchantment.BINDING_CURSE.getName();
        for (int i = 0; i < enchantmentRarity.length - 1; i++) {
            enchantmentRarity[i] = Enchantment.getByName(ench[i]);
        }
        config.set("flip_rate&enchantment", false);
        config.set("track_scroll_data", false);
        config.set("expo_successrate_determinant", .6);
        config.set("expo_enchantment_determinant", .6);
        config.set("expo_enchantment_level_determinant", .6);
        config.set("default_scroll_slots", 7);
        config.set("scroll.name", "Scroll of %ENCH%!");
        config.set("scroll.description", "- This scroll has a %SUCCESS% % chance to enchant the item with %ENCH% %LVL%");
        config.set("scroll.destroy_description", "- This scroll has a %DESTROY% % chance destroy the item if failed!");
        config.set("scroll.material", Material.PAPER.toString());
        config.set("scroll.round", 10);
        config.set("scroll.success_max", 100);
        config.set("scroll.success_min", 10);
        config.set("scroll.destroy_risk", 0);
        config.set("cleanslatescroll.name", "Cleanslate Scroll");
        config.set("cleanslatescroll.description", "- This Cleanslate Scroll has a %SUCCESS% % chance to give the item another scroll slot.");
        config.set("cleanslatescroll.destroy_description", "- This scroll has a %DESTROY% % chance destroy the item if failed!");
        config.set("cleanslatescroll.material", Material.EMPTY_MAP.toString());
        config.set("cleanslatescroll.round", 1);
        config.set("cleanslatescroll.success_max", 5);
        config.set("cleanslatescroll.success_min", 1);
        config.set("cleanslatescroll.destroy_risk", 20);
        config.set("darkscroll.name", "Dark Scroll of %ENCH%!");
        config.set("darkscroll.description", "- This Dark Scroll has a %SUCCESS% % chance to enchant the item with %ENCH% %LVL%");
        config.set("darkscroll.destroy_description", "- This scroll has a %DESTROY% % chance destroy the item if failed!");
        config.set("darkscroll.material", Material.MAP.toString());
        config.set("darkscroll.round", 20);
        config.set("darkscroll.success_max", 60);
        config.set("darkscroll.success_min", 20);
        config.set("darkscroll.destroy_risk", 50);
        config.set("chaosscroll.name", "Chaos Scroll");
        config.set("chaosscroll.description", "- This scroll has a %SUCCESS% % chance randomize the enchantments on the item!");
        config.set("chaosscroll.destroy_description", "- This scroll has a %DESTROY% % chance destroy the item if failed!");
        config.set("chaosscroll.material", Material.BOOK.toString());
        config.set("chaosscroll.round", 60);
        config.set("chaosscroll.success_max", 60);
        config.set("chaosscroll.success_min", 60);
        config.set("chaosscroll.destroy_risk", 50);
        config.set("safetyscroll.name", "Safety Scroll");
        config.set("safetyscroll.description", "- This scroll will protect the item from being destroyed by another scroll!");
        config.set("safetyscroll.destroy_description", "- This scroll will protect the item from being destroyed by another scroll!");
        config.set("safetyscroll.round", 100);
        config.set("safetyscroll.success_max", 100);
        config.set("safetyscroll.success_min", 100);
        config.set("safetyscroll.material", Material.LEATHER.toString());
        config.set("teleportscroll.material", Material.BOOK.toString());
        config.set("teleportscroll.deathrisk", 0);
        config.set("teleportscroll.distance_limit", 550);
        config.set("enchantmentworthe", ench);
    }

    @Override
    void loadToPlugin() {
        trackScrollData = config.getBoolean("track_scroll_data");
        flipRateToEnchantment = config.getBoolean("flip_rate&enchantment");
        expoSuccessRates = config.getDouble("expo_successrate_determinant");
        expoEnchantments = config.getDouble("expo_enchantment_determinant");
        expoLevel = config.getDouble("expo_enchantment_level_determinant");
        scrollSlots = config.getInt("default_scroll_slots");
        for (ScrollDataType factor : ScrollDataType.values()) {
            if (config.contains("scroll." + factor.toString().toLowerCase())) {
                scrolls.put(factor, config.get("scroll." + factor.toString().toLowerCase()));
            }
        }
        for (ScrollDataType factor : ScrollDataType.values()) {
            if (config.contains("darkscroll." + factor.toString().toLowerCase())) {
                darkScrolls.put(factor, config.get("darkscroll." + factor.toString().toLowerCase()));
            }
        }
        for (ScrollDataType factor : ScrollDataType.values()) {
            if (config.contains("cleanslatescroll." + factor.toString().toLowerCase())) {
                cleanSlateScrolls.put(factor, config.get("cleanslatescroll." + factor.toString().toLowerCase()));
            }
        }
        for (ScrollDataType factor : ScrollDataType.values()) {
            if (config.contains("chaosscroll." + factor.toString().toLowerCase())) {
                chaosScrolls.put(factor, config.get("chaosscroll." + factor.toString().toLowerCase()));
            }
        }
        for (ScrollDataType factor : ScrollDataType.values()) {
            if (config.contains("safetyscroll." + factor.toString().toLowerCase())) {
                safetyScrolls.put(factor, config.get("safetyscroll." + factor.toString().toLowerCase()));
            }
        }
        List<String> list = config.getStringList("enchantmentworthe");
        for (int i = 0; i < list.size() - 1; i++) {
            enchantmentRarity[i] = Enchantment.getByName(list.get(i));
        }
    }
public void setTrackScrollData(boolean b){
    if(b != trackScrollData)
        plugin.getScrollFactory().initTrack();
    trackScrollData = b;
    
}
    public void setScrollSlotLimit(int scrollLimit) {
        config.set("default_scroll_slots", scrollLimit);
        this.scrollSlots = scrollLimit;
    }
    public boolean trackData(){
        return trackScrollData;
    }
    public boolean flipRateAndRarity() {
        return flipRateToEnchantment;
    }

    public double getExpoSuccessRates() {
        return expoSuccessRates;
    }

    public double getExpoEnchantments() {
        return expoEnchantments;
    }

    public double getExpoLevel() {
        return expoLevel;
    }

    public int getScrollLimit() {
        return scrollSlots;
    }

    public Enchantment[] getEnchantmentPriority() {
        return this.enchantmentRarity;
    }

    public int getScrollData(ScrollDataType type) {
        return (int) scrolls.get(type);
    }

    public int getDarkScrollData(ScrollDataType type) {
        return (int) darkScrolls.get(type);
    }

    public int getCleanSlateScrollData(ScrollDataType type) {
        return (int) cleanSlateScrolls.get(type);
    }

    public int getChaosScrollData(ScrollDataType type) {
        return (int) chaosScrolls.get(type);
    }

    public int getSafetyScrollData(ScrollDataType type) {
        return (int) chaosScrolls.get(type);
    }

    public String getSafetyScrollDataAsString(ScrollDataType type) {
        return (String) scrolls.get(type);
    }

    public String getScrollDataAsString(ScrollDataType type) {
        return (String) scrolls.get(type);
    }

    public String getDarkScrollDataAsString(ScrollDataType type) {
        return (String) darkScrolls.get(type);
    }

    public String getCleanSlateScrollDataAsString(ScrollDataType type) {
        return (String) cleanSlateScrolls.get(type);
    }

    public String getChaosScrollDataAsString(ScrollDataType type) {
        return (String) chaosScrolls.get(type);
    }

    public Material getDarkScrollMaterial() {
        return Material.valueOf(darkScrolls.get(ScrollDataType.MATERIAL).toString());
    }

    public Material getSafetyScrollMaterial() {
        return Material.valueOf(safetyScrolls.get(ScrollDataType.MATERIAL).toString());
    }

    public Material getScrollMaterial() {
        return Material.valueOf(scrolls.get(ScrollDataType.MATERIAL).toString());
    }

    public Material getCleanSlateScrollMaterial() {
        return Material.valueOf(cleanSlateScrolls.get(ScrollDataType.MATERIAL).toString());
    }

    public Material getChaosScrollMaterial() {
        return Material.valueOf(chaosScrolls.get(ScrollDataType.MATERIAL).toString());
    }

}
