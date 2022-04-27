package me.goodestenglish.blockbreakcounter.listener;

import me.goodestenglish.blockbreakcounter.BlockBreakCounter;
import me.goodestenglish.blockbreakcounter.BlockBreakCounterItem;
import me.goodestenglish.core.utilities.Utilities;
import me.goodestenglish.core.utilities.serilization.LocationSerilization;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakCounterListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        Block block = event.getBlock();

        if (player.hasPermission("blockbreakcounter.admin") && itemStack.equals(BlockBreakCounterItem.SETTINGS_STICK)) {
            BlockBreakCounter.INSTANCE.getBlockBreakCounterManager().setBlockLocation(event.getBlock().getLocation());
            BlockBreakCounter.INSTANCE.getConfigFile().set("block-location", LocationSerilization.serializeLocation(event.getBlock().getLocation()));
            BlockBreakCounter.INSTANCE.getConfigFile().save();
            Utilities.sendMessage(player, BlockBreakCounter.INSTANCE.getConfigFile().getString("messages.successfully-set-block-location"), BlockBreakCounter.INSTANCE.getConfigFile().getString("messages.restart-required"));
            event.setCancelled(true);
            return;
        }

        if (block.getLocation().equals(BlockBreakCounter.INSTANCE.getBlockBreakCounterManager().getBlockLocation())) {
            BlockBreakCounter.INSTANCE.getBlockBreakCounterManager().handleBlockBreakCounter(player);
            event.setCancelled(true);
        }
    }

}
