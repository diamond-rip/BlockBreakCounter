package me.goodestenglish.blockbreakcounter.command;

import me.goodestenglish.blockbreakcounter.BlockBreakCounter;
import me.goodestenglish.blockbreakcounter.BlockBreakCounterItem;
import me.goodestenglish.core.commands.api.CommandArgs;
import me.goodestenglish.core.commands.api.MaidCommand;
import me.goodestenglish.core.commands.api.argument.CommandArguments;
import me.goodestenglish.core.utilities.Utilities;
import org.bukkit.entity.Player;

public class SetBlockCounterCommand extends MaidCommand {
    @CommandArgs(name = "setblockcounter", permission = "blockbreakcounter.command.setblockcounter")
    public void execute(CommandArguments command) {
        Player player = command.getPlayer();

        player.getInventory().setItemInHand(BlockBreakCounterItem.SETTINGS_STICK);
        Utilities.sendMessage(player, BlockBreakCounter.INSTANCE.getConfigFile().getString("messages.give-settings-stick"));
    }
}
