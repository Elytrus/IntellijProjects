package me.tlwv2.survivaladdon;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2017-12-08.
 */
public class DisplayManager {
    private HashMap<Player, BossBar> bars;

    public DisplayManager() {
        this.bars = new HashMap<>();
    }

    public void add(Player p){
        String uuid = p.getUniqueId().toString();
        int level = Addons.getInstance().manager().getLevel(uuid), points = Addons.getInstance().manager().getPoints(uuid);
        BossBar bar = Bukkit.createBossBar("\u00a7eLevel: " + level + " | Points: " + points, BarColor.GREEN, BarStyle.SOLID);
        bar.addPlayer(p);
        bar.setVisible(true);

        bars.put(p, bar);
    }

    public void remove(Player p){
        BossBar bar = bars.get(p);

        if(bar != null){
            bar.removeAll();
            bars.remove(p);
        }
    }

    public void enable(Player p){
        BossBar bar = bars.get(p);

        if(bar != null){
            bar.setVisible(true);
        }
    }

    public void disable(Player p){
        BossBar bar = bars.get(p);

        if(bar != null){
            bar.setVisible(false);
        }
    }

    public void update(){
        for(Map.Entry<Player, BossBar> entry : bars.entrySet()){
            Player p = entry.getKey();
            BossBar bar = entry.getValue();
            String uuid = p.getUniqueId().toString();
            int level = Addons.getInstance().manager().getLevel(uuid), points = Addons.getInstance().manager().getPoints(uuid);
            bar.setTitle("\u00a7eLevel: " + level + " | Points: " + points);
        }
    }

    public void destroy(){
        for(BossBar bar : bars.values()){
            bar.removeAll();
        }
        bars.clear();
    }
}
