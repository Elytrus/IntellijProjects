package me.tlwv2.factionsilkspawners;

import com.massivecraft.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Moses on 2017-09-05.
 */
public class FactionSilkSpawners extends JavaPlugin{
    @Override
    public void onEnable() {
        Plugin factionsPlugin = getServer().getPluginManager().getPlugin("Factions");
        if(factionsPlugin == null || !(factionsPlugin instanceof Factions)) {
            info("Factions plugin is invalid or not found!");
            setEnabled(false);
            return;
        }
        new EListener(this);

        info("v" + getDescription().getVersion() + " Enabled");
    }

    @Override
    public void onDisable() {
        info("v" + getDescription().getVersion() + " Disabled");
    }

    public static void info(String string) {
        Bukkit.getLogger().info("[FactionSilkSpawners] " + string);
    }
}
