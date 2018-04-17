package me.tlwv2.passiveaddons.enchantments.explosive;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Explosives {
    public static final Explosive EXPLOSIVE = new Explosive(300);
    public static final Fiery FIERY = new Fiery(301);
    public static final Safe SAFE = new Safe(302);

    public static void init(){
        Enchantment.registerEnchantment(EXPLOSIVE);
        Enchantment.registerEnchantment(FIERY);
        Enchantment.registerEnchantment(SAFE);
    }

    public static boolean isExplosive(ItemStack item){
        return item.getEnchantments().containsKey(EXPLOSIVE);
    }


}
