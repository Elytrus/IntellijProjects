package me.tlwv2.customitems.blocks;

import me.tlwv2.customitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2018-01-24.
 */
public class CustomBlocks implements Listener, ConfigurationSerializable{
    public HashMap<ItemStack, AbstractCustomBlock> blocklist;

    public HashMap<Location, AbstractCustomBlock> blocks;
    public HashMap<Location, IInteract> interacts;
    public HashMap<Location, IUpdate> updates;

    public BukkitRunnable updateTimer = new BukkitRunnable() {
        @Override
        public void run() {
            //;
        }
    };

    public CustomBlocks(Main plugin) {
        this.blocklist = new HashMap<>();
        this.blocks = new HashMap<>();
        this.interacts = new HashMap<>();
        this.updates = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        //;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        //
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        //
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }
}
