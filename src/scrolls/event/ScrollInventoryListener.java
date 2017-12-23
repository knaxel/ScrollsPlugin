
package scrolls.event;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;
import scrolls.ScrollFactory;
import scrolls.configuration.ScrollDataType;

/**
 *
 * @author knaxel
 */
public class ScrollInventoryListener implements Listener {

    private final ScrollFactory scrollFactory;

    public ScrollInventoryListener(ScrollFactory scrollfactory) {
        this.scrollFactory = scrollfactory;
    }

    private void permissionCheck(Player player) {
        player.sendMessage(ChatColor.RED + "You don't have permission to use that!");
        player.closeInventory();
    }

    // this method detects player inventory interaction and finds scrolls being used on items and calls the methods needed to apply the scrolls powers
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if ((event.getAction() != InventoryAction.PLACE_ALL && event.getAction() != InventoryAction.SWAP_WITH_CURSOR) || event.getCursor().getType() == Material.AIR || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        // a working way to detect scrolling (without tracking every single scroll)
        ItemStack scroll = event.getCursor();
        ItemMeta scrollMeta = scroll.getItemMeta();
        if (!scrollMeta.hasLore() || scrollMeta.getLore().size() < 1 || !scrollMeta.getLore().get(0).contains("Success Rate:") || !scrollMeta.getLore().contains("Success Rate:")) {
            return;
        }
        ItemStack appItem = event.getCurrentItem();
        ItemMeta appMeta = appItem.getItemMeta();
        if (appMeta.hasLore() && appMeta.getLore().size() > 0 && (appMeta.getLore().get(0).contains("Success Rate:") || appMeta.getLore().contains("Success Rate:"))) {
            return;
        }
        //we know we have a scroll after this
        event.setCancelled(true);
        if (!player.hasPermission("scrolls.use")) {
            permissionCheck(player);
            return;
        }
        if (scrollMeta.getDisplayName().equalsIgnoreCase(scrollFactory.getScrollConfig().getCleanSlateScrollDataAsString(ScrollDataType.NAME))) {
            useCleanslateScroll(scroll, appItem, player.getLocation());
            player.updateInventory();
            return;
        }
        if (scrollMeta.getLore().contains(("Scroll Slots: [0/" + scrollFactory.getScrollConfig().getScrollLimit() + "]"))) {
            return;
        }
        if (scrollMeta.getDisplayName().equalsIgnoreCase(scrollFactory.getScrollConfig().getChaosScrollDataAsString(ScrollDataType.NAME))) {
            if (!appMeta.hasEnchants() || appMeta.getEnchants().isEmpty()) {
                return;
            }
            useChaosScroll(scroll, appItem, player.getLocation());
            player.updateInventory();
            return;
        }
        if (scrollMeta.getEnchants().size() == 1) {
            useEnchantmentScroll(scroll, appItem, player.getLocation());
            player.updateInventory();
        }
    }

    //this method uses the cleanslate scroll algorithm on the appItem and plays a sound depending on the outcome
    private boolean useCleanslateScroll(ItemStack scroll, ItemStack appItem, Location location) {
        ItemMeta appMeta = appItem.getItemMeta();
        ItemMeta scrollMeta = scroll.getItemMeta();
        List<String> sLore = scrollMeta.getLore();
        if (Math.random() < (double) Integer.parseInt(sLore.get(0).split(" ")[2]) / 100) {
            addScrollSlot(appMeta);
            appItem.setItemMeta(appMeta);
            scroll.setAmount(scroll.getAmount() - 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_LAUNCH, 5, 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 10, .75f);
            return true;
        } else {
            if (sLore.size() > 1 && sLore.get(1).contains("Destroy Rate: ") && Math.random() < (double) Integer.parseInt(sLore.get(1).split(" ")[2]) / 100) {
                appItem.setAmount(0);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 15, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            } else {
                appItem.setItemMeta(appMeta);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            }
        }
    }

    //this method applies an enchantment scroll to the appitem and plays a sound depending on the outcome
    private boolean useEnchantmentScroll(ItemStack scroll, ItemStack appItem, Location location) {
        ItemMeta appMeta = appItem.getItemMeta();
        ItemMeta scrollMeta = scroll.getItemMeta();
        List<String> sLore = scrollMeta.getLore();
        useScrollSlot(appMeta);
        if (Math.random() < (double) Integer.parseInt(sLore.get(0).split(" ")[2]) / 100) {
            scrollMeta.getEnchants().keySet().forEach((ench) -> {
                int prev = 0;
                if (appMeta.getEnchants().get(ench) != null) {
                    prev = appMeta.getEnchantLevel(ench);
                }
                appMeta.addEnchant(ench, prev + scrollMeta.getEnchantLevel(ench), true);
            });
            appItem.setItemMeta(appMeta);
            scroll.setAmount(scroll.getAmount() - 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_LAUNCH, 5, 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 10, .75f);
            return true;
        } else {
            if (sLore.size() > 1 && sLore.get(1).contains("Destroy Rate: ") && Math.random() < (double) Integer.parseInt(sLore.get(1).split(" ")[2]) / 100) {
                appItem.setAmount(0);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 15, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            } else {
                appItem.setItemMeta(appMeta);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            }
        }
    }

    //this method applies the given chaosscroll to the appItem and plays a sound depending on its outcome
    private boolean useChaosScroll(ItemStack scroll, ItemStack appItem, Location location) {
        ItemMeta appMeta = appItem.getItemMeta();
        ItemMeta scrollMeta = scroll.getItemMeta();
        List<String> sLore = scrollMeta.getLore();
        useScrollSlot(appMeta);
        if (Math.random() < (double) Integer.parseInt(sLore.get(0).split(" ")[2]) / 100) {
            Enchantment[] enchs = scrollFactory.getScrollConfig().getEnchantmentPriority();
            appMeta.getEnchants().keySet().forEach((ench) -> {
                appMeta.removeEnchant(ench);
            });
            for (int i = 0; i < appItem.getItemMeta().getEnchants().size(); i++) {
                double f = Math.pow(Math.random(), scrollFactory.getScrollConfig().getExpoEnchantments());
                double l = Math.pow(Math.random(), scrollFactory.getScrollConfig().getExpoLevel());
                int max = enchs[((int) f * 10)].getMaxLevel();
                appMeta.addEnchant(enchs[(int) (f * 10)], (int) ((l * (max - 1)) + 1), true);
            }
            appItem.setItemMeta(appMeta);
            scroll.setAmount(scroll.getAmount() - 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_LAUNCH, 5, 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 10, .75f);
            return true;
        } else {
            if (sLore.size() > 1 && sLore.get(1).contains("Destroy Rate: ") && Math.random() < (double) Integer.parseInt(sLore.get(1).split(" ")[2]) / 100) {
                appItem.setAmount(0);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_BLAST, 15, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            } else {
                appItem.setItemMeta(appMeta);
                scroll.setAmount(scroll.getAmount() - 1);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_SHOOT, 5, 1);
                location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 10, .5f);
                return false;
            }
        }
    }

    //adds one mroe available scrolling slot to the itemmeta
    private void addScrollSlot(ItemMeta meta) {
        List<String> lore = new ArrayList<String>();
        int scrolllimit = scrollFactory.getScrollConfig().getScrollLimit();
        if (meta.hasLore()) {
            lore = meta.getLore();
            for (int i = 0; i <= scrolllimit; i++) {
                if (lore.contains(("Scroll Slots: [" + i + "/" + scrolllimit + "]"))) {
                    lore.set(lore.indexOf(("Scroll Slots: [" + i + "/" + scrolllimit + "]")), ("Scroll Slots: [" + (i + 1) + "/" + (scrolllimit + 1) + "]"));
                    break;
                }
                if (i == 7) {
                    lore = new ArrayList<>();
                    lore.add(0, "Scroll Slots: [" + (scrolllimit + 1) + "/" + (scrolllimit + 1) + "]");
                    break;
                }
            }
        } else {
            lore.add(0, "Scroll Slots: [" + (scrolllimit + 1) + "/" + (scrolllimit + 1) + "]");
        }
        meta.setLore(lore);
    }

    //subtracts one available scrolling slot to the itemmeta
    private void useScrollSlot(ItemMeta meta) {
        List<String> lore = new ArrayList<>();
        int scrolllimit = scrollFactory.getScrollConfig().getScrollLimit();
        if (meta.hasLore()) {
            lore = meta.getLore();
            for (int i = 0; i <= scrolllimit; i++) {
                if (lore.contains(("Scroll Slots: [" + i + "/" + scrolllimit + "]"))) {
                    lore.set(lore.indexOf(("Scroll Slots: [" + i + "/" + scrolllimit + "]")), ("Scroll Slots: [" + (i - 1) + "/" + scrolllimit + "]"));
                    break;
                }
                if (i == 7) {
                    lore = new ArrayList<>();
                    lore.add(0, "Scroll Slots: [" + (scrolllimit - 1) + "/" + scrolllimit + "]");
                    break;
                }
            }
        } else {
            lore.add(0, "Scroll Slots: [" + (scrolllimit - 1) + "/" + scrolllimit + "]");
        }
        meta.setLore(lore);
    }

    /* For the FUTURE
    //@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(event.getItem() == null) return;
        ItemStack scroll = event.getItem();
        ItemMeta scrollMeta = scroll.getItemMeta();
        if(!scroll.hasItemMeta())return;
        if (!scrollMeta.hasLore() || scrollMeta.getLore().size() < 1 || !scrollMeta.getLore().get(0).contains("Success Rate:")) {
            event.setCancelled(true);
            return;
        }
    }
     */
}
