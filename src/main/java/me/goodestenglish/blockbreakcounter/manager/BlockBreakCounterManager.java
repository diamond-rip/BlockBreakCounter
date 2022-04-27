package me.goodestenglish.blockbreakcounter.manager;

import lombok.Getter;
import lombok.Setter;
import me.goodestenglish.blockbreakcounter.BlockBreakCounter;
import me.goodestenglish.core.Maid;
import me.goodestenglish.core.utilities.Utilities;
import me.goodestenglish.core.utilities.hologram.Hologram;
import me.goodestenglish.core.utilities.serilization.LocationSerilization;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockBreakCounterManager {

    @Getter private int count;
    @Getter @Setter private Location blockLocation;
    @Getter private List<Hologram> holograms;

    public BlockBreakCounterManager() {
        String loc = BlockBreakCounter.INSTANCE.getConfigFile().getString("block-location");
        if (loc.equals("")) {
            return;
        }
        count = BlockBreakCounter.INSTANCE.getConfigFile().getInt("block-count");
        blockLocation = LocationSerilization.deserializeLocation(BlockBreakCounter.INSTANCE.getConfigFile().getString("block-location"));
        holograms = new ArrayList<>();

        double i = 0;
        List<String> strings = BlockBreakCounter.INSTANCE.getConfigFile().getStringList("block-holograms");
        Collections.reverse(strings);
        for (String str : strings) {
            holograms.add(new Hologram(blockLocation.clone().add(0.5, -0.5 + (i += Hologram.SPACE), 0.5), str, str.replaceAll("<count>", count + ""), true));
        }
    }

    public void handleBlockBreakCounter(Player player) {
        count++;
        for (Hologram hologram : holograms) {
            hologram.changeHologram(hologram.getOriginalLine().replaceAll("<count>", count + ""));
        }

        List<Integer> ints = BlockBreakCounter.INSTANCE.getConfigFile().getStringList("reward.execute-when-reached-block-count").stream().filter(Utilities::isInteger).map(Integer::parseInt).collect(Collectors.toList());
        if (ints.contains(count)) {
            List<String> commands = BlockBreakCounter.INSTANCE.getConfigFile().getStringList("reward.command");
            String[] command = commands.get(new Random().nextInt(commands.size())).replaceAll("<player>", player.getName()).split(";");

            spawnFirework(player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command[0]);
            Utilities.sendMessage(player, command[1]);
            Utilities.sendMessage(player, BlockBreakCounter.INSTANCE.getConfigFile().getString("messages.win-price").replaceAll("<count>", count + ""));
        }
    }

    private void spawnFirework(Player player) {
        ItemStack stackFirework = new ItemStack(Material.FIREWORK);
        FireworkMeta fireworkMeta = (FireworkMeta) stackFirework.getItemMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.AQUA).withFade(Color.AQUA).build());
        fireworkMeta.setPower(2);
        stackFirework.setItemMeta(fireworkMeta);

        EntityFireworks firework = new EntityFireworks(((CraftWorld) blockLocation.getBlock().getWorld()).getHandle(), blockLocation.getX() + 0.5, blockLocation.getY() + 2.0, blockLocation.getZ() + 0.5, CraftItemStack.asNMSCopy(stackFirework));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntity(firework, 76));

        Bukkit.getScheduler().runTaskLaterAsynchronously(Maid.INSTANCE, () -> {
            firework.expectedLifespan = 0;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(firework.getId(), firework.getDataWatcher(), true));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityStatus(firework, (byte) 17));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(firework.getId()));
        }, 2L);
    }

}
