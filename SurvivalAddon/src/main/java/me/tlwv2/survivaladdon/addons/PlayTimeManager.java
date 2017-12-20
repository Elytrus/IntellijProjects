package me.tlwv2.survivaladdon.addons;

import me.tlwv2.survivaladdon.Addons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2017-12-07.
 */
public class PlayTimeManager implements Listener, ConfigurationSerializable{
    public static final String POINT_DELAY_KEY = "period";
    public static final String POINT_INCREMENT_KEY = "increment";
    private int pointIncrement;
    private long pointDelay;
    private Addons plugin;

    private HashMap<String, BukkitRunnable> activeTimers;

    public PlayTimeManager(int pointIncrement, long pointDelay, Addons plugin) {
        this.pointIncrement = pointIncrement;
        this.pointDelay = pointDelay;
        this.plugin = plugin;

        this.activeTimers = new HashMap<>();
    }

    public PlayTimeManager(Map<String, Object> map){
        this.pointIncrement = (int) map.get(POINT_INCREMENT_KEY);
        this.pointDelay = (long) map.get(POINT_DELAY_KEY);
        this.plugin = (Addons) Bukkit.getPluginManager().getPlugin(Addons.PLUGIN_NAME);

        this.activeTimers = new HashMap<>();
    }

    public void addPlayer(Player p){
        String uuid = p.getUniqueId().toString();
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                Addons.getInstance().manager().setPoints(uuid, Addons.getInstance().manager().getPoints(uuid) + pointIncrement);
                Addons.getInstance().checkLevel(uuid);
            }
        };
        this.activeTimers.put(uuid, timer);
        timer.runTaskTimer(plugin, 0, this.pointDelay);
    }

    public void removePlayer(Player p){
        String uuid = p.getUniqueId().toString();
        BukkitRunnable timer = this.activeTimers.get(uuid);

        if(timer != null){
            timer.cancel();
            this.activeTimers.remove(uuid);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put(POINT_DELAY_KEY, pointDelay);
        map.put(POINT_INCREMENT_KEY, pointIncrement);

        return map;
    }
}
