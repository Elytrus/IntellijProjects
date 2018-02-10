package me.tlwv2.skyblockfly;

import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Moses on 2018-02-07.
 */
public class SkyblockFly extends JavaPlugin{
    public static SkyblockFly plugin;

    @Override
    public void onDisable() {
        //
    }

    @Override
    public void onEnable() {
        plugin = this;

        ILWrapper.registerPlugin(this);
        Bukkit.getPluginCommand("islandfly").setExecutor(new IslandFlyCommand());
    }
}
