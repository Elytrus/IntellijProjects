package me.tlwv2.customenchants;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created by Moses on 2018-02-07.
 */
public class EListener implements Listener{
    public EListener(CustomEnchants plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
