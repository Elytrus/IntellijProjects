package me.tlwv2.core.utils;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

/*
 * Makes it easier to handle nbt data
 */
public class ItemData {
    public static boolean hasFlag(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        return c.hasKey(flagname);
    }

    public static org.bukkit.inventory.ItemStack setFlag(org.bukkit.inventory.ItemStack item, String flagname){
        net.minecraft.server.v1_12_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.setBoolean(flagname, true);
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    public static org.bukkit.inventory.ItemStack setValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        net.minecraft.server.v1_12_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.set(flagname, new NBTTagInt(value));
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    public static double getInt(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        if(!hasFlag(item, flagname))
            return 0;
        else
            return c.getInt(flagname);
    }

    public static org.bukkit.inventory.ItemStack addValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getInt(item, flagname) + value);
    }

    public static org.bukkit.inventory.ItemStack multValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getInt(item, flagname) * value);
    }

    public static org.bukkit.inventory.ItemStack divValue(org.bukkit.inventory.ItemStack item, String flagname, int value){
        return setValue(item, flagname, getInt(item, flagname) / value);
    }

    ///////////////////////////////////////

    public static org.bukkit.inventory.ItemStack setValue(org.bukkit.inventory.ItemStack item, String flagname, double value){
        net.minecraft.server.v1_12_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.set(flagname, new NBTTagDouble(value));
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    public static double getDouble(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        if(!hasFlag(item, flagname))
            return 0.0;
        else
            return c.getInt(flagname);
    }

    public static org.bukkit.inventory.ItemStack addValue(org.bukkit.inventory.ItemStack item, String flagname, double value){
        return setValue(item, flagname, getDouble(item, flagname) + value);
    }

    public static org.bukkit.inventory.ItemStack multValue(org.bukkit.inventory.ItemStack item, String flagname, double value){
        return setValue(item, flagname, getDouble(item, flagname) * value);
    }

    public static org.bukkit.inventory.ItemStack divValue(org.bukkit.inventory.ItemStack item, String flagname, double value){
        return setValue(item, flagname, getDouble(item, flagname) / value);
    }

    /////////////////////////////////////////////////

    public static org.bukkit.inventory.ItemStack setValue(org.bukkit.inventory.ItemStack item, String flagname, String value){
        net.minecraft.server.v1_12_R1.ItemStack citem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = getTagCompoundOf(item);

        c.set(flagname, new NBTTagString(value));
        citem.setTag(c);

        return CraftItemStack.asBukkitCopy(citem);
    }

    public static String getString(org.bukkit.inventory.ItemStack item, String flagname){
        NBTTagCompound c = getTagCompoundOf(item);

        if(!hasFlag(item, flagname))
            return "";
        else
            return c.getString(flagname);
    }

    //////////////////////////////////////////////////////////////////////////////////////

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
