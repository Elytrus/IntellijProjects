package me.tlwv2.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.tlwv2.core.command.ListDependsCommand;
import me.tlwv2.core.command.PHelpCommand;
import me.tlwv2.core.infolist.ILWrapper;

public class Core extends JavaPlugin {
    public static Core self;

    @Override
    public void onEnable(){
        self = this;
        Constants.init();
        ILWrapper.registerPlugin(self);

        Bukkit.getPluginCommand("listdependers").setExecutor(new ListDependsCommand());
        Bukkit.getPluginCommand("phelp").setExecutor(new PHelpCommand());

        getLogger().info("Initialized Core Plugin");
    }
}
