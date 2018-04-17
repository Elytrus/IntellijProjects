package me.tlwv2.passiveaddons;

import me.tlwv2.core.misc.Glow;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.kitsp.Main;
import me.tlwv2.passiveaddons.enchantments.debuff.Debuffs;
import me.tlwv2.passiveaddons.enchantments.explosive.Explosives;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

/**
 * Created by Moses on 2017-10-10.
 */
public class Addons extends JavaPlugin{
    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        new EListener(this);

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {e.printStackTrace();}

        Debuffs.init();
        Explosives.init();

        getLogger().info("Plugin Started!");
    }
}
