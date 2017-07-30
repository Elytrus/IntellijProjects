package me.tlwv2.core.misc;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Glow extends Enchantment {
    public static final Glow glow = new Glow();

    public static Enchantment init(){
        Enchantment gloww;

        if(Enchantment.getByName(" ") == null){
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {e.printStackTrace();}

            try{
                Enchantment.registerEnchantment(Glow.glow);
                Bukkit.getLogger().info("Registered Enchantment");
            } catch(Exception e){}

            gloww = Glow.glow;
        }
        else{
            Bukkit.getLogger().info("Getting Enchantment");
            gloww = Enchantment.getByName(" ");
        }

        return gloww;
    }

    public Glow() {
        super(100); //Problem?
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean canEnchantItem(ItemStack arg0) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        // TODO Auto-generated method stub
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return " ";
    }

    @Override
    public int getStartLevel() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean isCursed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTreasure() {
        // TODO Auto-generated method stub
        return true;
    }

}
