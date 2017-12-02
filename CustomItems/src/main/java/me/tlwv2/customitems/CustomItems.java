package me.tlwv2.customitems;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Moses on 2017-10-06.
 */
public class CustomItems extends JavaPlugin{
    private static CustomItems self;

    private HashMap<ItemStack, CustomItem> itemMap;
    private List<CustomItem> activeItems;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        self = this;

        itemMap = new HashMap<>();
        activeItems = new ArrayList<>();
    }

    static void registerCustomItem(CustomItem item){

    }

    public static CustomItems getInstance(){
        return self;
    }
}
