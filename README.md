# ScrollsPlugin

(in the process of revamping the entire plugin) (too messy and just bad)
A plugin that adds scrolls to the game spigot/bukkit


Current Scrolls:

Basic Scroll
- The basic scroll will have an enchantment and level that can be applied to an item.

Dark Scroll
- The Dark Scroll will add an enchantment to the item twice as strong as the basic scroll - It is intended to have the high chance of destroying the item if the scroll fails.

Clean Slate Scroll
- The Clean Slate Scroll will return one scroll slot to the item if the scroll is success full. (obviously will not use a scroll slot)
- It is intended to have the off chance of destroying the item.

Chaos Scroll
- Will grab each of the enchantments and change them at random.
- It is intended to have a decent chance of destroying the item if failed, but a high chance of success.

The first thing to note in the config is the EnchantmentWorth category, this establishes how valued each enchantment is, giving the option to make enchantments more rare than others.

flip_rate&enchantment : this is not implemented yet

Basic "rarity" algorithm:

to determine how rare each item is in comparison to the other I use a simple algorithm. The server generates two numbers each between 1 and 0. one of those numbers is multiplied to the power of a DETERMINANT value in the config. The larger to the DETERMINANT value is, the more exponential the "rarity" of each item is.(this algorithm is also used in the chaos scroll when changing items enchantments)

Here's a diagram to demonstrate how it effects the plugin.

https://www.spigotmc.org/attachments/determinant-png.285791/

expo_successrate_determinant: used for the rate of success for each scroll

expo_enchantment_determinant: used the enchantment rarity

expo_enchantment_level_determinant: used for the enchantment level rarity

default_scroll_slots: sets the amount of slots each item has by default

all of the success and destroy rates are by percent. So if success_rate_maximum was 10, the max success rate that type of scroll has is 10%. The round attribute determines the rate of the percents a scroll goes up by. for example,the default round is set to 10 so the percentages possible for the basic scroll type are 10,20,30,40,50,60,70,80,90,100
