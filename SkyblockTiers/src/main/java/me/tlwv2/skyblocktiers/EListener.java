package me.tlwv2.skyblocktiers;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created by Moses on 2017-08-22.
 */
public class EListener implements Listener{
    public EListener(SkyblockTiers plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
