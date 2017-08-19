package me.tlwv2.flyingboats;

import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses on 2017-08-12.
 */
public class FlyingBoats extends JavaPlugin{
    public static final String BOAT_NAME = "Flying Boat";

    public static FlyingBoats self;

    private List<Boat> boats = new ArrayList<>();
    private List<Boat> stoppedBoats = new ArrayList<>();

    @Override
    public void onDisable() {
        for(Boat b : boats){
            b.setCustomName("");
            b.setCustomNameVisible(false);
        }
        boats.clear();
        stoppedBoats.clear();
    }

    @Override
    public void onEnable() {
        self = this;
        new EListener(this);
        ILWrapper.registerPlugin(self);
        Bukkit.getPluginCommand("spawnflyingboat").setExecutor(new SpawnFlyingBoatCommand());
    }

    public void spawnFlyingBoat(Player p){
        Location l = p.getLocation();
        Boat b = (Boat) p.getWorld().spawnEntity(l, EntityType.BOAT);
        b.setCustomName(BOAT_NAME);
        b.setCustomNameVisible(true);
        boats.add(b);
    }

    public Boat getFlyingBoat(Player p){
        for(Boat b : boats){
            if(b.getPassengers().contains(p))
                return b;
        }
        return null;
    }

    public boolean isInFlyingBoat(Player p){
        return getFlyingBoat(p) != null;
    }

    public boolean isFlyingBoat(Entity e){
        return boats.contains(e);
    }

    public void remove(Entity e){
        boats.remove(e);
    }

    public int toggleStopped(Boat b){
        if(!boats.contains(b)){
            getLogger().severe("attempting to toggle non flying boat???");
            return -1;
        }

        if(stoppedBoats.contains(b)) {
            stoppedBoats.remove(b);
            return 1;
        }
        stoppedBoats.add(b);
        return 0;
    }

    public boolean isStopped(Boat b){
        return stoppedBoats.contains(b);
    }
}
