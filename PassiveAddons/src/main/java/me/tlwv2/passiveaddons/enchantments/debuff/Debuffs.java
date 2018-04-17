package me.tlwv2.passiveaddons.enchantments.debuff;

import me.tlwv2.passiveaddons.enchantments.debuff.effect.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Debuffs {
    public static final Blindness BLINDNESS = new Blindness(200);
    public static final Hunger HUNGER = new Hunger(201);
    public static final MiningFatigue MINING_FATIGUE = new MiningFatigue(202);
    public static final Nausea NAUSEA = new Nausea(203);
    public static final Poison POISON = new Poison(204);
    public static final Slow SLOW = new Slow(205);
    public static final Weakness WEAKNESS = new Weakness(206);
    public static final Wither WITHER = new Wither(207);

    public static final Duration DURATION = new Duration(210);

    public static final List<Enchantment> DEBUFFS = Arrays.asList(BLINDNESS, HUNGER, MINING_FATIGUE, NAUSEA, POISON,
            SLOW, WEAKNESS, WITHER);

    //INIT WORK
    public static void init(){
        for(Enchantment enchantment : DEBUFFS){
            Enchantment.registerEnchantment(enchantment);
        }
        Enchantment.registerEnchantment(DURATION);
    }

    public static boolean hasDebuff(ItemStack item){
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        return enchantments.keySet().stream().anyMatch(DEBUFFS::contains) && enchantments.containsKey(DURATION);
    }

    public static boolean hasDebuff(Projectile projectile){

    }

    public static ArrayList<PotionEffect> getEffects(ItemStack item){
        int duration = item.getEnchantmentLevel(DURATION);
        ArrayList<PotionEffect> effects = new ArrayList<>();
        for(Map.Entry<Enchantment, Integer> enchantment : item.getEnchantments().entrySet()){
            Enchantment type = enchantment.getKey();
            if(isDebuff(type)){
                effects.add(new PotionEffect(typeFor(type), duration, enchantment.getValue()));
            }
        }
        return effects;
    }

    public static ArrayList<PotionEffect> getEffects(Projectile projectile){

    }

    public static void mapOntoProjectile(ItemStack origin, Projectile projectile){
        
    }

    public static PotionEffectType typeFor(Enchantment enchantment){
        if (BLINDNESS.equals(enchantment)) {
            return PotionEffectType.BLINDNESS;
        }
        else if (HUNGER.equals(enchantment)) {
            return PotionEffectType.HUNGER;
        }
        else if (MINING_FATIGUE.equals(enchantment)) {
            return PotionEffectType.SLOW_DIGGING;
        }
        else if (NAUSEA.equals(enchantment)) {
            return PotionEffectType.CONFUSION;
        }
        else if (POISON.equals(enchantment)) {
            return PotionEffectType.POISON;
        }
        else if (SLOW.equals(enchantment)) {
            return PotionEffectType.SLOW;
        }
        else if (WEAKNESS.equals(enchantment)) {
            return PotionEffectType.WEAKNESS;
        }
        else if (WITHER.equals(enchantment)) {
            return PotionEffectType.WITHER;
        }
        else {
            return null;
        }
    }

    public static boolean isDebuff(Enchantment enchantment){
        return DEBUFFS.contains(enchantment);
    }
}
