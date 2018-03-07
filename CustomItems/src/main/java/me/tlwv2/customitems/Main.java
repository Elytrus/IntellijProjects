package me.tlwv2.customitems;

import me.tlwv2.customitems.blocks.CustomBlocks;
import me.tlwv2.customitems.entities.CustomEntities;
import me.tlwv2.customitems.items.CustomItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Moses on 2017-10-06.
 */
public class Main extends JavaPlugin{
    public static Main self;

    public CustomItems items;
    public CustomBlocks blocks; //WIP
    public CustomEntities entities; //WIP

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        self = this;

        items = new CustomItems(this);
        blocks = new CustomBlocks(this);
        entities = new CustomEntities(this);
    }
}
