package me.tlwv2.skyblocktiers;

import me.tlwv2.core.Constants;
import me.tlwv2.core.utils.ItemUtil;
import org.apache.commons.lang3.ArrayUtils;
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
import us.talabrek.ultimateskyblock.api.event.RestartIslandEvent;

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
        if(isCobble(e.getBlock(), to)){
            //infoPlayers("cobble generated");
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
            ItemStack item = e.getCurrentItem();

            if(null != item){
                int tier = SkyblockTiers.self.getTier(p);
                double balance = SkyblockTiers.self.getEconomy().getBalance(p);

                if(ArrayUtils.contains(SkyblockTiers.UPGRADE_ITEMS, item.getType())) {
                    if(tier > 4){
                        previewItem(i, item, "\u00a7cMax tier already reached!", e.getSlot(), 20L);
                    }
                    else if (SkyblockTiers.self.canBuy(p)) {
                        SkyblockTiers.self.buy(p);

                        SkyblockTiers.self.buildUpgradeInventory(i, tier + 1,
                                balance - SkyblockTiers.UPGRADE_COSTS[tier]);
                        previewItem(i, i.getItem(e.getSlot()), "\u00A7aSuccess!", e.getSlot(), 20L);
                    }
                    else{
                        previewItem(i, item, "\u00A7cInsufficient Funds!", e.getSlot(), 20L);
                    }
                }
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRestart(RestartIslandEvent e){
        Player p = e.getPlayer();
        SkyblockTiers.self.setTier(p.getName(), 0);
        p.sendMessage(Constants.WARN + "Mining upgrades will be reset!");
    }

    int isFluid(Material m){
        switch(m){
            case STATIONARY_WATER:
                return 1;
            case WATER:
                return 1;
            case STATIONARY_LAVA:
                return 2;
            case LAVA:
                return 2;
            default:
                return 0;
        }
    }

    boolean isCobble(Block a, Block b){
        Material m = b.getType();
        int ft = isFluid(a.getType());

        if(ft == 0)
            return false;

        if(isAnyTypeSurrounding(b, ft == 2 ? Material.WATER : Material.LAVA, ft == 2 ? Material.STATIONARY_WATER
        : Material.STATIONARY_LAVA))
            return true;
        return false;
    }

    boolean isAnyTypeSurrounding(Block b, Material m, Material m2){
        final int[][] MAT_LIST = {
                {-1, 0, 0},
                {1, 0, 0},
                {0, -1, 0},
                {0, 1, 0},
                {0, 0, 1},
                {0, 0, -1}
        };
        Location l = b.getLocation();

        //infoPlayers(m + " | " + m2);

        for(int[] dirs : MAT_LIST){
            Material currentType = l.clone().add(dirs[0], dirs[1], dirs[2]).getBlock().getType();
            if(currentType == m || currentType == m2)
                return true;
        }
        return false;
    }

    //debug
    void infoPlayers(Object message){
        Bukkit.getServer().getOnlinePlayers().forEach(e -> e.sendMessage(message.toString()));
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