package scrolls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import scrolls.configuration.ScrollDataType;
import scrolls.configuration.ScrollsConfig;
import scrolls.maths.ScrollsUtil;

/**
 *
 * @author knaxel
 */
public class ScrollFactory {

    private final ScrollsConfig config;
    private Map<Enchantment, double[][]> enchScrollData;
    private Map<Enchantment, double[]> darkScrollData;
    private double[][] cleanScrollData;
    private double[][] chaosScrollData;

    public ScrollFactory(ScrollsConfig config) {
        this.config = config;
        if (config.trackData()) {
            initTrack();
        }

    }

    public void purgeScrollData() {

        enchScrollData.clear();
        darkScrollData.clear();

    }

    public Map<Enchantment, double[][]> getEnchScrollData() {
        return enchScrollData;
    }

    public Map<Enchantment, double[]> getDarkScrollData() {
        return darkScrollData;
    }

    public double[][] getCleanScrollData() {
        return cleanScrollData;
    }

    public double[][] getChaosScrollData() {
        return chaosScrollData;
    }

    public void initTrack() {

        enchScrollData = new HashMap<>();
        for (Enchantment ench : Enchantment.values()) {
            if (config.getScrollData(ScrollDataType.DESTROY_RISK) == 0) {
                enchScrollData.put(ench, new double[((int) 100 / config.getScrollData(ScrollDataType.ROUND))][1]);
            } else {

            }
        }
        darkScrollData = new HashMap<>();
        for (Enchantment ench : Enchantment.values()) {
            darkScrollData.put(ench, new double[((int) 100 / config.getDarkScrollData(ScrollDataType.ROUND))]);
        }
        cleanScrollData = new double[((int) (100 / config.getCleanSlateScrollData(ScrollDataType.ROUND)))][((int) config.getCleanSlateScrollData(ScrollDataType.DESTROY_RISK) / config.getCleanSlateScrollData(ScrollDataType.ROUND))];
        chaosScrollData = new double[((int) (100 / config.getChaosScrollData(ScrollDataType.ROUND)))][((int) config.getChaosScrollData(ScrollDataType.DESTROY_RISK) / config.getChaosScrollData(ScrollDataType.ROUND))];

    }

    //returns configuration
    public ScrollsConfig getScrollConfig() {
        return this.config;
    }

    //returns a random enchantment based off enchantmentpriority / value
    private Enchantment getRandomEnchanment() {
        Enchantment[] enchs = config.getEnchantmentPriority();
        double f;
        if (config.getExpoEnchantments() == 0) {
            f = Math.random();
        } else {
            f = 1 - Math.pow(Math.random(), config.getExpoEnchantments());
        }
        Enchantment ench = enchs[(int) Math.floor(f * 10)];
        String name = ench.getName();
        return ench;
    }

    //returns a random success rate in increments of lockTo and between max and min
    private int success(int lockTo, double max, double min) {
        max = max / 100;
        min = min / 100;
        double base = 0;
        base = Math.random() * Math.pow(Math.random(), config.getExpoSuccessRates());
        base = base * (max - min) + min;
        return (int) Math.floor(base * (100) / lockTo) * lockTo;
    }

    //returns enchantscroll
    public ItemStack getRandomEnchantmentScroll() {
        //Some variables well need declared
        ItemStack scroll = new ItemStack(config.getScrollMaterial());
        ItemMeta meta = scroll.getItemMeta();
        List<String> lore = new ArrayList<String>();
        //getting our scroll stats
        Enchantment chose = getRandomEnchanment();
        int level = 1 + (int) ((Math.pow(Math.random(), config.getExpoLevel()) * (chose.getMaxLevel() - 1)));
        meta.addEnchant(chose, level, true);
        int rate = success(config.getScrollData(ScrollDataType.ROUND),
                config.getScrollData(ScrollDataType.SUCCESS_MAX),
                config.getScrollData(ScrollDataType.SUCCESS_MIN));
        int risk = config.getScrollData(ScrollDataType.DESTROY_RISK) - success(config.getScrollData(ScrollDataType.ROUND), config.getScrollData(ScrollDataType.DESTROY_RISK), 0);
        //turning our enchantments we chose into english for the scroll meta
        String dname = "";
        String roman = "";
        for (Enchantment ench : meta.getEnchants().keySet()) {
            dname = ench.getName().toLowerCase().replace("_", " ");
            roman = ScrollsUtil.IntegerToRoman(meta.getEnchants().get(ench));
        }
        //scroll strings
        String name = config.getScrollDataAsString(ScrollDataType.NAME).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        String desc = config.getScrollDataAsString(ScrollDataType.DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        String ddesc = config.getScrollDataAsString(ScrollDataType.DESTROY_DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        //applying the scroll data
        lore.add(0, "Success Rate: " + rate + " %");
        meta.setDisplayName(name);
        lore.add(ChatColor.GRAY + desc);
        if (risk != 0) {
            lore.add(ChatColor.DARK_GRAY + ddesc);
            lore.add(1, "Destroy Rate: " + risk + " %");
        }
        meta.setLore(lore);
        scroll.setItemMeta(meta);

        if (config.trackData()) {
            enchScrollData.get(chose)[rate / config.getScrollData(ScrollDataType.ROUND)][risk / config.getScrollData(ScrollDataType.ROUND)] += 1;
        }

        return scroll;
    }

    //returns darkscroll
    public ItemStack getRandomDarkEnchantmentScroll() {
        //Some variables well need declared
        ItemStack scroll = new ItemStack(config.getDarkScrollMaterial());
        ItemMeta meta = scroll.getItemMeta();
        List<String> lore = new ArrayList<String>();
        //getting our scroll stats
        Enchantment chose = getRandomEnchanment();
        int level = 1 + (int) (Math.pow(Math.random(), config.getExpoLevel()) * (chose.getMaxLevel() - 1) * 2); //my way of making things rarer ;)
        meta.addEnchant(chose, level, true);
        int rate = success(config.getDarkScrollData(ScrollDataType.ROUND), config.getDarkScrollData(ScrollDataType.SUCCESS_MAX), config.getDarkScrollData(ScrollDataType.SUCCESS_MIN));
        int risk = config.getDarkScrollData(ScrollDataType.DESTROY_RISK);
        //turning our enchantments we chose into english for the scroll meta
        String dname = "";
        String roman = "";
        for (Enchantment ench : meta.getEnchants().keySet()) {
            dname = ench.getName().toLowerCase().replace("_", " ");
            roman = ScrollsUtil.IntegerToRoman(meta.getEnchants().get(ench));
        }
        //scroll strings
        String name = config.getDarkScrollDataAsString(ScrollDataType.NAME).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        String desc = config.getDarkScrollDataAsString(ScrollDataType.DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        String ddesc = config.getDarkScrollDataAsString(ScrollDataType.DESTROY_DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%ENCH%", dname).replace("%LVL%", roman).replace("%DESTROY%", risk + "");
        //applying the scroll data
        meta.setDisplayName(name);
        lore.add(0, "Success Rate: " + rate + " %");
        lore.add(ChatColor.GRAY + desc);
        if (risk != 0) {
            lore.add(ChatColor.DARK_GRAY + ddesc);
            lore.add(1, "Destroy Rate: " + risk + " %");
        }
        meta.setLore(lore);
        scroll.setItemMeta(meta);
        if (config.trackData()) {
            darkScrollData.get(chose)[rate / config.getDarkScrollData(ScrollDataType.ROUND)] += 1;
        }
        return scroll;
    }

    //returns chaosscroll
    public ItemStack getRandomChaosScroll() {
        //Some variables well need declared
        ItemStack scroll = new ItemStack(config.getChaosScrollMaterial());
        ItemMeta meta = scroll.getItemMeta();
        List<String> lore = new ArrayList<String>();
        //getting our scroll stats
        int rate = success(config.getChaosScrollData(ScrollDataType.ROUND), config.getChaosScrollData(ScrollDataType.SUCCESS_MAX), config.getChaosScrollData(ScrollDataType.SUCCESS_MIN));
        int risk = config.getChaosScrollData(ScrollDataType.DESTROY_RISK) - success(config.getChaosScrollData(ScrollDataType.ROUND), config.getChaosScrollData(ScrollDataType.DESTROY_RISK), 0);
        //scroll strings
        String name = config.getChaosScrollDataAsString(ScrollDataType.NAME);
        String desc = config.getChaosScrollDataAsString(ScrollDataType.DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%DESTROY%", risk + "");
        String ddesc = config.getChaosScrollDataAsString(ScrollDataType.DESTROY_DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%DESTROY%", risk + "");
        //applying scroll data
        lore.add(0, "Success Rate: " + rate + " %");
        meta.setDisplayName(name);
        lore.add(ChatColor.GRAY + desc);
        if (risk != 0) {
            lore.add(1, "Destroy Rate: " + risk + " %");
            lore.add(ChatColor.DARK_GRAY + ddesc);
        }
        meta.setLore(lore);
        scroll.setItemMeta(meta);
        if (config.trackData()) {
            chaosScrollData[rate / config.getChaosScrollData(ScrollDataType.ROUND)][risk / config.getChaosScrollData(ScrollDataType.ROUND)] += 1;
        }
        return scroll;
    }

    //returns cleanslatescroll
    public ItemStack getRandomCleanSlateScroll() {
        //Some variables well need declared
        ItemStack scroll = new ItemStack((config.getCleanSlateScrollMaterial()));
        ItemMeta meta = scroll.getItemMeta();
        List<String> lore = new ArrayList<String>();
        //getting our scroll stats
        int rate = success(config.getCleanSlateScrollData(ScrollDataType.ROUND), config.getCleanSlateScrollData(ScrollDataType.SUCCESS_MAX), config.getCleanSlateScrollData(ScrollDataType.SUCCESS_MIN));

        int risk = config.getCleanSlateScrollData(ScrollDataType.DESTROY_RISK) - success(config.getCleanSlateScrollData(ScrollDataType.ROUND), config.getCleanSlateScrollData(ScrollDataType.DESTROY_RISK), 0);
        //scroll strings
        String name = config.getCleanSlateScrollDataAsString(ScrollDataType.NAME);
        String desc = config.getCleanSlateScrollDataAsString(ScrollDataType.DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%DESTROY%", risk + "");
        String ddesc = config.getCleanSlateScrollDataAsString(ScrollDataType.DESTROY_DESCRIPTION).replace("%SUCCESS%", rate + "").replace("%DESTROY%", risk + "");
        //applying the scroll data
        meta.setDisplayName(name);
        lore.add(0, "Success Rate: " + rate + " %");
        lore.add(ChatColor.GRAY + desc);
        if (risk != 0) {
            lore.add(1, "Destroy Rate: " + risk + " %");
            lore.add(ChatColor.DARK_GRAY + ddesc);
        }
        meta.setLore(lore);
        scroll.setItemMeta(meta);
        if (config.trackData()) {
            cleanScrollData[rate / config.getCleanSlateScrollData(ScrollDataType.ROUND)][risk / config.getCleanSlateScrollData(ScrollDataType.ROUND)] += 1;
        }
        return scroll;
    }

    // 4 tha future!
    /*public ItemStack getSaftyScroll(){
        
        ItemStack scroll = new ItemStack((config.getSafetyScrollMaterial()));
        ItemMeta meta = scroll.getItemMeta();
        List<String> lore = new ArrayList<String>();
        //----------------------------------------------
        
        int rate = success(config.getSafetyScrollData(ScrollDataType.ROUND),
                config.getSafetyScrollData(ScrollDataType.SUCCESS_MAX),
                config.getSafetyScrollData(ScrollDataType.SUCCESS_MIN));
        
        lore.add(0, "Success Rate: " + rate  + " %");
        
        meta.setDisplayName(config.getSafetyScrollDataAsString(ScrollDataType.NAME));
        lore.add(ChatColor.GRAY + config.getScrollDataAsString(ScrollDataType.DESRCIPTION));
        
        meta.setLore(lore);
        scroll.setItemMeta(meta);
        return scroll;
    }*/
}
