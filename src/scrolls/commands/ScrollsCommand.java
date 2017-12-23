
package scrolls.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import scrolls.ScrollsPlugin;

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
        player.sendMessage(message + "--------------------------------------------------\n");
    }
    //if anyone knows of a better way to handle subcommands, let me know because this looks horrible and cannot be the most efficient way i am sure.
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] arg) {
        if (cs instanceof Player) {
            Player player = (Player) cs;
            if (arg.length == 0) {
                sendHelp(player);
                return true;
            }
            switch (arg[0]) {
                default: {
                    sendHelp(player);
                    return true;
                }
                case "help": {
                    sendHelp(player);
                    return true;
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
        }
        return true;
    }

}
