package me.tlwv2.factionsilkspawners;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class EListener implements Listener {
    public EListener(FactionSilkSpawners instance) {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(block.getType() != Material.MOB_SPAWNER) {
            return;
        }

        if(!player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            return;
        }

        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(block.getLocation()));
        MPlayer mPlayer = MPlayer.get(player);

        if(faction == null || mPlayer == null) {
            return;
        }

        if(faction.equals(mPlayer.getFaction()) &&
                !faction.equals(FactionColl.get().getSafezone()) &&
                !faction.equals(FactionColl.get().getWarzone()) &&
                !faction.equals(FactionColl.get().getNone())) {
            return;
        }

        event.setCancelled(false);
    }
}
