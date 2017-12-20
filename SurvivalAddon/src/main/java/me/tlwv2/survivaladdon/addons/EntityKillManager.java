package me.tlwv2.survivaladdon.addons;

import me.tlwv2.survivaladdon.Addons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Moses on 2017-12-07.
 */
public class EntityKillManager implements Listener, ConfigurationSerializable{
    public static final String TYPE_KEY = "type";
    public static final String POINT_AMOUNT_KEY = "pointAmount";

    private String type;
    private int pointAmount;

    public EntityKillManager(String type, int amount, Addons plugin) {
        this.type = type;
        this.pointAmount = amount;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public EntityKillManager(Map<String, Object> map){
        this.type = (String) map.get(TYPE_KEY);
        this.pointAmount = (int) map.get(POINT_AMOUNT_KEY);
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin(Addons.PLUGIN_NAME));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getClass().getSimpleName().equals(type) && e.getEntity().isDead()){
            if(e.getDamager() instanceof Player){
                String uuid = e.getDamager().getUniqueId().toString();
                Addons.getInstance().manager().setPoints(uuid, Addons.getInstance().manager().getPoints(uuid) + this.pointAmount);
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TYPE_KEY, type);
        map.put(POINT_AMOUNT_KEY, pointAmount);

        return map;
    }
}
