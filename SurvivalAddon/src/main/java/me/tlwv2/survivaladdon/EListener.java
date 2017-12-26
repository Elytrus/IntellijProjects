package me.tlwv2.survivaladdon;

import me.tlwv2.core.Constants;
import me.tlwv2.survivaladdon.addons.EntityKillManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            Addons.getInstance().manager().setLevel(uuid, 0);
            Addons.getInstance().manager().setMultiplier(uuid, 1.0f);
        }
        Addons.getInstance().getDisplayManager().add(p);
        Addons.getInstance().getPlayTimeManager().addPlayer(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Addons.getInstance().getDisplayManager().remove(p);
        Addons.getInstance().getPlayTimeManager().removePlayer(p);
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent e){
        Player p = e.getPlayer();
        p.sendMessage(Constants.GOOD + p.getDisplayName() + Constants.GOOD + " you are now level " + e.getNewLevel() + "!");
        Addons.getInstance().getDisplayManager().update();
    }
}
