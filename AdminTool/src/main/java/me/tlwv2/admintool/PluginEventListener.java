package me.tlwv2.admintool;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.ArrayList;
import java.util.Collection;

public class PluginEventListener implements Listener {
    public PluginEventListener() {
        AdminTool plugin = AdminTool.self;

        if(plugin == null)
            throw new NullPointerException("§4Error 0: Plugin instance not initialized " + AdminTool.askMSG);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e){
        //idek something is wrong so i did the true check just in case
        if(AdminTool.self.table().getRawBoolean("onePlayerSleep")){
            Player p = e.getPlayer();

            Collection<Player> onlines = new ArrayList<Player>();
            onlines.addAll(p.getServer().getOnlinePlayers());
            onlines.remove(p);
            for(Player pp : onlines)
                pp.sendMessage("§6§lPlayer " + p.getDisplayName() + "§6§l has slept!");

            p.getWorld().setTime(1000);
            p.setBedSpawnLocation(e.getBed().getLocation().add(0, 1, 0));
            p.sendMessage("§6You have now slept!");
            e.setCancelled(true);
        }
    }
}
