package me.tlwv2.survivaladdon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by Moses on 2017-11-23.
 */
public abstract class Addon implements Listener {
    protected double pointCount;
    protected double multiplier;
    protected boolean enabled;
    protected Player host;
    protected String hostId;

    public Addon(JavaPlugin plugin, double pointCount, double multiplier, Player host) {
        this.pointCount = pointCount;
        this.multiplier = multiplier;
        this.host = host;
        this.hostId = host.getUniqueId().toString();
        this.enabled = true;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void reward(){
        force();
        Addons.getInstance().getPoints().editPoints(this.hostId, this.pointCount * this.multiplier);
        force();
    }

    public void spend(double points){
        force();
        Addons.getInstance().getPoints().editPoints(this.hostId, -points);
        force();
    }

    public double get(){
        force();
        return Addons.getInstance().getPoints().getPoints(this.hostId);
    }

    public void force(){
        Addons.getInstance().getPoints().forceUpdate();
    }

    public void setMultiplier(double multiplier){
        this.multiplier = multiplier;
    }

    public abstract void die();
}
