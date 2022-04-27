package me.goodestenglish.blockbreakcounter;

import lombok.Getter;
import me.goodestenglish.blockbreakcounter.command.SetBlockCounterCommand;
import me.goodestenglish.blockbreakcounter.listener.BlockBreakCounterListener;
import me.goodestenglish.blockbreakcounter.manager.BlockBreakCounterManager;
import me.goodestenglish.core.utilities.file.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBreakCounter extends JavaPlugin {

    public static BlockBreakCounter INSTANCE;
    @Getter private ConfigFile configFile;

    @Getter private BlockBreakCounterManager blockBreakCounterManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        configFile = new ConfigFile(this, "config.yml");
        blockBreakCounterManager = new BlockBreakCounterManager();

        new SetBlockCounterCommand();
        getServer().getPluginManager().registerEvents(new BlockBreakCounterListener(), this);
    }

    @Override
    public void onDisable() {
        configFile.set("block-count", blockBreakCounterManager.getCount());
        configFile.save();
    }
}
