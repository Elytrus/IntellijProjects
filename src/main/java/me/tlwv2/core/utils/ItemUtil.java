package me.tlwv2.core.utils;

import me.tlwv2.core.Constants;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemUtil {
    public static ItemStack addMetadata(ItemStack i, String name, boolean isGlow, String... lore){
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        if(isGlow) im.addEnchant(Constants.glow, 1, true);
        if(lore.length > 0) im.setLore(Arrays.asList(lore));
        i.setItemMeta(im);

        return i;
    }

    public static void addEnch(ItemStack i, Enchantment[] enchs, int[] levels){
        if(enchs.length != levels.length)
            throw new IllegalArgumentException("enchs array and levels array are different lengths! pls call maxwell and fix");

        for(int j = 0; j < enchs.length; j++)
            i.addUnsafeEnchantment(enchs[j], levels[j]);
    }

    public static ItemStack addEnchChainable(ItemStack i, Enchantment[] enchs, int[] levels){
        addEnch(i, enchs, levels);
        return i;
    }

    public static ItemStack skull(String name){
        ItemStack i = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta)i.getItemMeta();
        meta.setOwner(name);
        i.setItemMeta(meta);
        return i;
    }
}
