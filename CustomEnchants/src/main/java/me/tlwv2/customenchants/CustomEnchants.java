package me.tlwv2.customenchants;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class CustomEnchants extends JavaPlugin{
    public static CustomEnchants self;

    public BukkitRunnable updateLoop = new BukkitRunnable() {
        @Override
        public void run() {
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                //
            }
        }
    };

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        //Basic Init...
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Registering Enchants...

        //End Stuff...
        self = this;
        new EListener(self);

        updateLoop.runTaskTimer(self, 0, 1);
    }

    public void registerEnchantment(Enchantment enchantment){
        try{
            Enchantment.registerEnchantment(enchantment);
            Bukkit.getLogger().info("Registered Enchantment " + enchantment.getName());
        } catch(Exception e){}
    }
}
