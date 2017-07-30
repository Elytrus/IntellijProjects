package me.tlwv2.factionfly.api;

import me.tlwv2.factionfly.FactionFly;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2017-07-28.
 */
public class TimedQueue implements Listener{
    private HashMap<Player, Integer> queue = new HashMap<>();
    private HashMap<Player, PlayerProgressBar> bars = new HashMap<>();
    private BukkitRunnable timer;
    private int queuelength;

    public TimedQueue(FactionFly plugin, String name) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        timer = new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Player, Integer> entry : queue.entrySet()){
                    if(entry.getValue() == 1)
                        queue.remove(entry.getKey());
                    else
                        queue.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        };
        timer.runTaskTimer(plugin, 0, 1);
    }
}
