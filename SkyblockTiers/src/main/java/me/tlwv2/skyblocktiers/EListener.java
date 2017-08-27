package me.tlwv2.skyblocktiers;

import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import us.talabrek.ultimateskyblock.api.IslandInfo;

/**
 * Created by Moses on 2017-08-22.
 */
public class EListener implements Listener{
    public EListener(SkyblockTiers plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent e){
        Block to = e.getToBlock();
        Material type = to.getType();
        if(type == Material.COBBLESTONE || type == Material.STONE){
            IslandInfo island = SkyblockTiers.self.getSkyblock().getIslandInfo(e.getBlock().getLocation());
            int tier = SkyblockTiers.self.getTier(island.getLeader());
            Material changeTo = SkyblockTiers.self.rollForBlock(tier);

            if(changeTo != null){
                to.setType(changeTo);
                Location l = to.getLocation();
                Firework f = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
                FireworkMeta meta = f.getFireworkMeta();
                meta.setPower(2);
                FireworkEffect eff = FireworkEffect.builder()
                        .withColor(Color.PURPLE)
                        .withFade(Color.BLACK)
                        .withFlicker()
                        .with(FireworkEffect.Type.BURST)
                        .build();
                meta.addEffect(eff);
                f.setFireworkMeta(meta);
                f.playEffect(EntityEffect.FIREWORK_EXPLODE);
                Bukkit.getScheduler().scheduleSyncDelayedTask(SkyblockTiers.self, () -> {
                    f.remove();
                }, 2L);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Inventory i = e.getInventory();

        if(i.getName() != null && i.getName().equals(SkyblockTiers.UPGRADE_INVENTORY_NAME)){
            Player p = (Player)e.getWhoClicked();

            e.setCancelled(true);
        }
    }

    private void previewItem(Inventory inventory, ItemStack itemStack, String text, int slot, long previewLength){
        ItemStack previewStack = new ItemStack(Material.EMPTY_MAP, 1);
        previewStack = ItemUtil.addMetadata(previewStack, text, true);

        inventory.setItem(slot, previewStack);

        Bukkit.getScheduler().scheduleSyncDelayedTask(SkyblockTiers.self, () -> {
            inventory.setItem(slot, itemStack);
        }, previewLength);
    }
}