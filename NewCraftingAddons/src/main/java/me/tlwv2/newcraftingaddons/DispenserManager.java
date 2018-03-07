package me.tlwv2.newcraftingaddons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Moses on 2018-03-02.
 */
public class DispenserManager implements Listener {
    private final CraftingAddons main;

    public DispenserManager(CraftingAddons main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onDispenser(BlockDispenseEvent e){
        ItemStack i = e.getItem();
        Material m = i.getType();
        int d = getMaxDurability(m);

        if(d == -1){
            return;
        }
    }

    private boolean isPick(Material m){
        return m == Material.WOOD_PICKAXE || m == Material.STONE_PICKAXE || m == Material.IRON_PICKAXE || m == Material.GOLD_PICKAXE
                || m == Material.DIAMOND_PICKAXE;
    }

    private boolean isAxe(Material m){
        return m == Material.WOOD_AXE || m == Material.STONE_AXE || m == Material.IRON_AXE || m == Material.GOLD_AXE
                || m == Material.DIAMOND_AXE;
    }

    private boolean isSword(Material m){
        return m == Material.WOOD_SWORD || m == Material.STONE_SWORD || m == Material.IRON_SWORD || m == Material.GOLD_SWORD
                || m == Material.DIAMOND_SWORD;
    }

    private boolean isShovel(Material m){
        return m == Material.WOOD_SPADE || m == Material.STONE_SPADE || m == Material.IRON_SPADE || m == Material.GOLD_SPADE
                || m == Material.DIAMOND_SPADE;
    }

    private boolean isHoe(Material m){
        return m == Material.WOOD_HOE || m == Material.STONE_HOE || m == Material.IRON_HOE || m == Material.GOLD_HOE
                || m == Material.DIAMOND_HOE;
    }

    private int getMaxDurability(Material m){
        if(m == Material.WOOD_PICKAXE || m == Material.WOOD_AXE || m == Material.WOOD_SWORD || m == Material.WOOD_SPADE ||
                m == Material.WOOD_HOE){
            return 60;
        }
        else if(m == Material.STONE_PICKAXE || m == Material.STONE_AXE || m == Material.STONE_SWORD || m == Material.STONE_SPADE ||
                m == Material.STONE_HOE){
            return 132;
        }
        else if(m == Material.IRON_PICKAXE || m == Material.IRON_AXE || m == Material.IRON_SWORD || m == Material.IRON_SPADE ||
                m == Material.IRON_HOE){
            return 251;
        }
        else if(m == Material.GOLD_PICKAXE || m == Material.GOLD_AXE || m == Material.GOLD_SWORD || m == Material.GOLD_SPADE ||
                m == Material.GOLD_HOE){
            return 33;
        }
        else if(m == Material.DIAMOND_PICKAXE || m == Material.DIAMOND_AXE || m == Material.DIAMOND_SWORD || m == Material.DIAMOND_SPADE ||
                m == Material.DIAMOND_HOE){
            return 1561;
        }
        else if(m == Material.SHEARS){
            return 238;
        }

        return -1;
    }

    private boolean willTakeDamage(ItemStack i){
        ItemMeta m = i.getItemMeta();
        if(!m.hasEnchant(Enchantment.DURABILITY)){
            return true;
        }

        int level = m.getEnchantLevel(Enchantment.DURABILITY);

        return Math.random() < 1 / (double)(level + 1);
    }
}
