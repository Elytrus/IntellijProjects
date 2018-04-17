package me.tlwv2.minions.nbt;

import me.tlwv2.core.Constants;
import me.tlwv2.core.utils.ItemData;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Moses on 2018-03-23.
 */
public class EffectHandler {

    public static final String EFFECT_KEY = "minionPotionEffects";
    public static final String TYPE_KEY = "type";
    public static final String DURATION_KEY = "duration";
    public static final String AMPLIFIER_KEY = "amplifier";

    public EffectHandler() {

    }

    public boolean hasEffects(ItemStack item){
        return ItemData.hasFlag(item, EFFECT_KEY);
    }

    public ItemStack addPotionEffect(ItemStack item, PotionEffectType type, int duration, int amplifier) {
        NBTTagCompound compound = ItemData.getTagCompoundOf(item), effect = new NBTTagCompound();

        effect.setString(TYPE_KEY, type.getName());
        effect.setInt(DURATION_KEY, duration);
        effect.setInt(AMPLIFIER_KEY, amplifier);

        NBTTagList effects = null;

        if (hasEffects(item)) {
            effects = compound.getList(EFFECT_KEY, 10);
        }
        else{
            effects = new NBTTagList();
        }

        effects.add(compound);
        compound.set(EFFECT_KEY, effects);

        return ItemData.setTagCompoundOf(item, compound);
    }

    public ItemStack removePotionEffect(ItemStack item, PotionEffectType type){

    }

    public String listPotionEffects(String prepend, ItemStack item){
        if(!hasEffects(item)){
            return Constants.WARN + "Invalid Item!";
        }

        NBTTagList effects = ItemData.getTagCompoundOf(item).getList(EFFECT_KEY, 10);
        String f = prepend;
        int l = effects.size();

        for(int i = 0; i < l; i++){
            NBTTagCompound effect = effects.get(i);
            f += "\n"
        }
    }
}
