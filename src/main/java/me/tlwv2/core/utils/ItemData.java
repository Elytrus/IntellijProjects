package me.tlwv2.core.utils;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

import net.minecraft.server.v1_11_R1.Block;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagInt;

/*
 * Makes it easier to handle nbt data
 */
public class ItemData {
    public static boolean hasFlag(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        return c.hasKey(flagname);
    }

    public static org.bukkit.inventory.ItemStack setFlag(org.bukkit.inventory.ItemStack item, String flagname){
        net.minecraft.server.v1_11_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.setBoolean(flagname, true);
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    public static org.bukkit.inventory.ItemStack setValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        net.minecraft.server.v1_11_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.set(flagname, new NBTTagInt(value));
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    public static int getValue(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        if(!hasFlag(item, flagname))
            return 0;
        else
            return c.getInt(flagname);
    }

    public static org.bukkit.inventory.ItemStack addValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getValue(item, flagname) + value);
    }

    public static org.bukkit.inventory.ItemStack multValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getValue(item, flagname) * value);
    }

    public static org.bukkit.inventory.ItemStack divValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getValue(item, flagname) / value);
    }

    public static NBTTagCompound getTagCompoundOf(org.bukkit.inventory.ItemStack item){
        ItemStack nmsitem = CraftItemStack.asNMSCopy(item);

        if(nmsitem == null){
            nmsitem = new ItemStack(Block.getByName("minecraft:stone"));
            nmsitem.setTag(new NBTTagCompound());
        }

        if(!nmsitem.hasTag() || nmsitem.getTag() == null)
            nmsitem.setTag(new NBTTagCompound());

        return nmsitem.getTag();
    }

    public static org.bukkit.inventory.ItemStack setTagCompoundOf(org.bukkit.inventory.ItemStack item, NBTTagCompound tags){
        ItemStack citem = CraftItemStack.asNMSCopy(item);
        citem.setTag(tags);

        return CraftItemStack.asBukkitCopy(citem);
    }
}
