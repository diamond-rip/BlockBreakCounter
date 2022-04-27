package me.goodestenglish.blockbreakcounter;

import me.goodestenglish.core.utilities.chat.CC;
import me.goodestenglish.core.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockBreakCounterItem {

    public static ItemStack SETTINGS_STICK = new ItemBuilder(Material.STICK)
            .name(CC.SECONDARY + "Use " + CC.PRIMARY + "ME" + CC.SECONDARY + " to set a block location!")
            .build();

}
