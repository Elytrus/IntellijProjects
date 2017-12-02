package me.tlwv2.customitems;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2017-10-06.
 */
public abstract class CustomItem implements Listener{
    private ItemStack item;

    public CustomItem(){
        Bukkit.getPluginManager().registerEvents(this, CustomItems.getInstance());
    }

    public abstract ItemStack getItemINIT();
    public abstract void onTick();
}
