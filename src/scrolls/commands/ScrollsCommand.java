
package scrolls.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import scrolls.ScrollsPlugin;
import scrolls.configuration.ScrollDataType;

/**
 *
 * @author knaxel
 */
public class ScrollsCommand implements CommandExecutor {

    private final ScrollsPlugin plugin;

    public ScrollsCommand(ScrollsPlugin plugin) {
        this.plugin = plugin;
    }

    private void correctUse(Player player, String rest) {
        player.sendMessage(ChatColor.WHITE + "CorrectUsage \n" + ChatColor.RED + "'/scrolls " + rest + "'");
    }

    private void permissionCheck(Player player) {
        player.sendMessage(ChatColor.RED + "You don't have permission for that command!");
    }

    private void permissionCheck(CommandSender cs) {
        cs.sendMessage(ChatColor.RED + "You don't have permission for that command!");
    }

    private void sendHelp(Player player) {
        String message = "--------------------------------------------------\n"
                + ChatColor.GOLD + "                 SCROLLS HELPS 1\n" + ChatColor.WHITE;
        int count = 0;
        if (player.hasPermission("scrolls.command.get")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls get <type>" + ChatColor.WHITE + " - will get you a random type of scroll.\n");
        }
        if (player.hasPermission("scrolls.command.get.more")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls get <type> <amount>" + ChatColor.WHITE + "- will get you many scrolls of the same type\n");
        }
        if (player.hasPermission("scrolls.command.help")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls help" + ChatColor.WHITE + "- will show you this message.\n");
        }
        if (player.hasPermission("scrolls.command.reload")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls reload" + ChatColor.WHITE + "- will reload the plugins config file.\n");
        }
        if (player.hasPermission("scrolls.command.data.toggle")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls data <true/false>" + ChatColor.WHITE + "- will reload the plugins config file.\n");
        }
        if (player.hasPermission("scrolls.command.data.view")) {
            count++;
            message += (count + "" + ChatColor.GRAY + ". /scrolls data view" + ChatColor.WHITE + "- will reload the plugins config file.\n");
        }
        player.sendMessage(message + "--------------------------------------------------\n");
    }

    //if anyone knows of a better way to handle subcommands, let me know because this looks horrible and cannot be the most efficient way i am sure.
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] arg) {
        if (!(cs instanceof Player)) {
        }
        Player player = (Player) cs;
        if (arg.length == 0) {
            sendHelp(player);
            return true;
        }
        switch (arg[0].toLowerCase()) {
            default: {
                sendHelp(player);
                return true;
            }
            case "help": {
                sendHelp(player);
                return true;
            }
            case "data": {
                if (!player.hasPermission("scrolls.command.data")) {
                    permissionCheck(player);
                    return true;
                }
                if (arg.length < 2) {
                    int count = 1;
                    String message = "------------------Scroll Data-------------------\n";
                    if (player.hasPermission("scrolls.command.data.toggle")) {
                        count++;
                        message += (count + "" + ChatColor.GRAY + ". /scrolls data <true/false>" + ChatColor.WHITE + "- will reload the plugins config file.\n");
                    }
                    if (player.hasPermission("scrolls.command.data.view")) {
                        count++;
                        message += (count + "" + ChatColor.GRAY + ". /scrolls data view <page> [ignore enchantmets (true/false)]" + ChatColor.WHITE + "- will reload the plugins config file.\n");
                    }
                    message += "------------------------------------------------";
                    cs.sendMessage(message);
                    return true;
                }
                switch (arg[1].toLowerCase()) {
                    case "true": {
                        if (!player.hasPermission("scrolls.command.data.toggle")) {
                            permissionCheck(player);
                            return true;
                        }
                        plugin.getScrollFactory().getScrollConfig().setTrackScrollData(true);
                        cs.sendMessage("scroll data tracking is on.");
                        return true;
                    }
                    case "false": {
                        if (!player.hasPermission("scrolls.command.data.toggle")) {
                            permissionCheck(player);
                            return true;
                        }
                        plugin.getScrollFactory().getScrollConfig().setTrackScrollData(false);
                        plugin.getScrollFactory().purgeScrollData();
                        cs.sendMessage("scroll data tracking is off.");
                        return true;
                    }
                    case "view": {
                        if (!player.hasPermission("scrolls.command.data.view")) {
                            permissionCheck(player);
                            return true;
                        }
                        if (!plugin.getScrollFactory().getScrollConfig().trackData()) {
                            cs.sendMessage(ChatColor.RED + "That command can only be used when data tracking is enabled!\nuse '/scroll data <true/false>' to toggle data tracking.\nWARNING : this turns off when the server reloads unless you save the config file specifically");
                            return true;
                        }
                        int page = 0;
                        int pagelg = 33;
                        if (arg.length >= 3) {
                            try {
                                page = Integer.parseInt(arg[2]);
                            } catch (NumberFormatException e) {
                                cs.sendMessage(ChatColor.RED + "That was not a page number?");
                                return true;
                            }
                        }
                        String message = "";
                        message += ChatColor.BLUE + "Basic Scroll Data" + ChatColor.WHITE + " : \n-----------------------------------------------\n";
                        Map<Enchantment, double[][]> scrollData = plugin.getScrollFactory().getEnchScrollData();
                        for (Enchantment ench : Enchantment.values()) {
                            boolean empty = true;
                            double[][] data = scrollData.get(ench);
                            for (int i = 0; i < data.length; i++) {
                                if (data[i] != null && data[i].length != 0) {
                                    for (int j = 0; j < data[i].length; j++) {
                                        if (data[i][j] != 0) {
                                            if (empty) {
                                                message += ChatColor.RED + "%15s" + ChatColor.WHITE + " : \n\n";
                                                message = String.format(message, ench.getName());
                                                empty = false;
                                            }
                                            message += "success rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "     destroy rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "   total made:" + ChatColor.GOLD + "%6s\n\n" + ChatColor.WHITE;
                                            message = String.format(message, i * plugin.getScrollFactory().getScrollConfig().getScrollData(ScrollDataType.ROUND), j * plugin.getScrollFactory().getScrollConfig().getScrollData(ScrollDataType.ROUND), data[i][j]);
                                        }
                                    }
                                }
                            }
                        }
                        message += ChatColor.BLUE + "Dark Scroll Data" + ChatColor.WHITE + " : \n-----------------------------------------------\n";
                        Map<Enchantment, double[]> darkScrollData = plugin.getScrollFactory().getDarkScrollData();
                        for (Enchantment ench : Enchantment.values()) {
                            boolean empty = true;
                            double[] data = darkScrollData.get(ench);
                            for (int i = 0; i < data.length; i++) {
                                if (data[i] != 0) {
                                    if (empty) {
                                        message += ChatColor.RED + "%15s" + ChatColor.WHITE + " : \n\n";
                                        message = String.format(message, ench.getName());
                                        empty = false;
                                    }
                                    message += "success rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "     destroy rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "   total made:" + ChatColor.GOLD + "%6s\n\n" + ChatColor.WHITE;
                                    message = String.format(message, i * plugin.getScrollFactory().getScrollConfig().getDarkScrollData(ScrollDataType.ROUND), plugin.getScrollFactory().getScrollConfig().getDarkScrollData(ScrollDataType.DESTROY_RISK), data[i]);
                                }
                            }
                        }
                        message += ChatColor.BLUE + "Clean Scroll Data" + ChatColor.WHITE + " : \n-----------------------------------------------\n";
                        double[][] data = plugin.getScrollFactory().getCleanScrollData();
                        for (int i = 0; i < data.length; i++) {
                            for (int j = 0; j < data[i].length; j++) {
                                if (data[i][j] != 0) {
                                    message += "success rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "     destroy rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "   total made:" + ChatColor.GOLD + "%6s\n\n" + ChatColor.WHITE;
                                    message = String.format(message, i * plugin.getScrollFactory().getScrollConfig().getCleanSlateScrollData(ScrollDataType.ROUND), plugin.getScrollFactory().getScrollConfig().getCleanSlateScrollData(ScrollDataType.DESTROY_RISK), data[i][j]);
                                }
                            }
                        }
                        message += ChatColor.BLUE + "Chaos Scroll Data" + ChatColor.WHITE + " : \n-----------------------------------------------\n";
                        double[][] cdata = plugin.getScrollFactory().getChaosScrollData();
                        for (int i = 0; i < cdata.length; i++) {
                            for (int j = 0; j < cdata[i].length; j++) {
                                if (data[i][j] != 0) {
                                    message += "success rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "     destroy rate:" + ChatColor.GOLD + "%4s" + ChatColor.WHITE + "   total made:" + ChatColor.GOLD + "%6s\n\n" + ChatColor.WHITE;
                                    message = String.format(message, i * plugin.getScrollFactory().getScrollConfig().getChaosScrollData(ScrollDataType.ROUND), plugin.getScrollFactory().getScrollConfig().getChaosScrollData(ScrollDataType.DESTROY_RISK), cdata[i][j]);
                                }
                            }
                        }
                        message += ChatColor.DARK_GRAY + "-----------------------------------------------\n";
                        String[] lines = message.split("\n");
                        try {
                            File log = new File(plugin.getDataFolder() + "/scrolldata-" + System.nanoTime() / 1000 + ".txt");
                            FileWriter fw = new FileWriter(log);
                            for (String line : lines) {
                                fw.write(line.replaceAll("§c", "").replaceAll("§f", "").replaceAll("§6", "").replaceAll("§9", "").replaceAll("§8", "") + "\n");
                            }
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String finalmessage = ChatColor.DARK_GRAY + "----------------" + ChatColor.WHITE + "Scroll Data [ " + ChatColor.BLUE + page + ChatColor.WHITE + " ]" + ChatColor.DARK_GRAY + "-----------------\n" + ChatColor.WHITE;
                        page = page * pagelg;
                        if (lines.length - page * pagelg < pagelg) {
                            page = lines.length - pagelg;
                        }
                        try {
                            for (int i = 0; i < pagelg; i++) {
                                if (lines.length > i + page) {
                                    finalmessage += lines[i + page] + "\n";
                                } else {
                                    break;
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        cs.sendMessage(ChatColor.RED + "There is not enough data to show you. A file may have still been made.");
                        }
                        cs.sendMessage(finalmessage);
                        return true;
                    }
                    default: {
                        int count = 1;
                        String message = "------------------Scroll Data-------------------\n";
                        if (player.hasPermission("scrolls.command.data.toggle")) {
                            count++;
                            message += (count + "" + ChatColor.GRAY + ". /scrolls data <true/false>" + ChatColor.WHITE + "- will reload the plugins config file.\n");
                        }
                        if (player.hasPermission("scrolls.command.data.view <page>")) {
                            count++;
                            message += (count + "" + ChatColor.GRAY + ". /scrolls data view <page> [ignore enchantmets (true/false)]" + ChatColor.WHITE + "- will reload the plugins config file.\n");
                        }
                        message += "------------------------------------------------";
                        cs.sendMessage(message);
                        return true;
                    }
                }
            }
            case "get": {
                if (!player.hasPermission("scrolls.command.get")) {
                    permissionCheck(player);
                    return true;
                }
                if (arg.length < 2) {
                    if (player.hasPermission("scrolls.command.get.more")) {
                        correctUse(player, "get <type> <amount>");
                        player.sendMessage(ChatColor.GRAY + "Types of scrolls include: darkscroll , basicscroll,chaosscroll, and cleanslatescroll");
                        return true;
                    }
                    correctUse(player, "get <type>");
                    player.sendMessage(ChatColor.GRAY + "Types of scrolls include: darkscroll , basicscroll,chaosscroll, and cleanslatescroll");
                    return true;
                }
                switch (arg[1].toLowerCase()) {
                    case "cleanslatescroll": {
                        int i = 1;
                        if (player.hasPermission("scrolls.command.get.more")) {
                            try {
                                i = Integer.parseInt(arg[2]);
                            } catch (NumberFormatException e) {
                                correctUse(player, "get <type> <amount>");
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.getInventory().addItem(plugin.getScrollFactory().getRandomCleanSlateScroll());
                                return true;
                            }
                        }
                        while (i > 0) {
                            player.getInventory().addItem(plugin.getScrollFactory().getRandomCleanSlateScroll());
                            i--;
                        }
                        return true;
                    }
                    case "darkscroll": {
                        int i = 1;
                        if (player.hasPermission("scrolls.command.get.more")) {
                            try {
                                i = Integer.parseInt(arg[2]);
                            } catch (NumberFormatException e) {
                                correctUse(player, "get <type> <amount>");
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.getInventory().addItem(plugin.getScrollFactory().getRandomDarkEnchantmentScroll());
                                return true;
                            }
                        }
                        while (i > 0) {
                            player.getInventory().addItem(plugin.getScrollFactory().getRandomDarkEnchantmentScroll());
                            i--;
                        }
                        return true;
                    }
                    case "basicscroll": {
                        int i = 1;
                        if (player.hasPermission("scrolls.command.get.more")) {
                            try {
                                i = Integer.parseInt(arg[2]);
                            } catch (NumberFormatException e) {
                                correctUse(player, "get <type> <amount>");
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.getInventory().addItem(plugin.getScrollFactory().getRandomEnchantmentScroll());
                                return true;
                            }
                        }
                        while (i > 0) {
                            player.getInventory().addItem(plugin.getScrollFactory().getRandomEnchantmentScroll());
                            i--;
                        }
                        return true;
                    }
                    case "chaosscroll": {
                        int i = 1;
                        if (player.hasPermission("scrolls.command.get.more")) {
                            try {
                                i = Integer.parseInt(arg[2]);
                            } catch (NumberFormatException e) {
                                correctUse(player, "get <type> <amount>");
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.getInventory().addItem(plugin.getScrollFactory().getRandomChaosScroll());
                                return true;
                            }
                        }
                        while (i > 0) {
                            player.getInventory().addItem(plugin.getScrollFactory().getRandomChaosScroll());
                            i--;
                        }
                        return true;
                    }
                    default: {
                        if (player.hasPermission("scrolls.command.get.more")) {
                            correctUse(player, "get <type> <amount>");
                            player.sendMessage(ChatColor.GRAY + "Types of scrolls include: darkscroll , basicscroll,chaosscroll, and cleanslatescroll");
                            return true;
                        }
                        correctUse(player, "get <type>");
                        player.sendMessage(ChatColor.GRAY + "Types of scrolls include: darkscroll , basicscroll,chaosscroll, and cleanslatescroll");
                        return true;
                    }
                }
            }
            case "reload": {
                if (!player.hasPermission("scrolls.command.reload")) {
                    permissionCheck(player);
                    return true;
                }
                plugin.reload();
                player.sendMessage("Success!");
            }
        }
        return false;
    }

}
