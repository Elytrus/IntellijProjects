package me.tlwv2.survivaladdon;


import net.iceblaze.PointMan.API.PointManAPI;
import net.iceblaze.PointMan.Main.Main;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Moses on 2017-11-22.
 */
public class Addons extends JavaPlugin {
    private static Addons instance;

    private PointManAPI points;

    @Override
    public void onDisable() {
        //dd
    }

    @Override
    public void onEnable() {
        this.points = Main.getPointManAPI();
        instance = this;
    }

    public static Addons getInstance(){
        return instance;
    }

    public PointManAPI getPoints(){
        return points;
    }
}
