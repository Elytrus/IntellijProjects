package me.tlwv2.survivaladdon;

import me.tlwv2.survivaladdon.addons.EntityKillManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Moses on 2017-12-07.
 */
public class EListener implements Listener{
    public EListener(Addons plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        if(!Addons.getInstance().manager().points(uuid)){
            Addons.getInstance().manager().setPoints(uuid, 0);
            Addons.getInstance().manager().setLevel(uuid, 1);
            Addons.getInstance().manager().setMultiplier(uuid, 1.0f);
        }
        Addons.getInstance().getDisplayManager().add(p);
        Addons.getInstance().getPlayTimeManager().addPlayer(p);
    }
}
