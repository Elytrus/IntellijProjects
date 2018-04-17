package me.tlwv2.minions.nbt;

import me.tlwv2.core.utils.ItemData;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Moses on 2018-03-23.
 */
public class BasicsHandler {
    public static final String EGG_FLAG = "minionEgg";
    public static final String ENTITY_TYPE_KEY = "entityType";

    public BasicsHandler() {

    }

    public boolean isMinionEgg(ItemStack item){
        return ItemData.hasFlag(item, EGG_FLAG);
    }

    public boolean isValidType(String typeName){
        try{
            EntityType.valueOf(typeName);
            return true;
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }

    public String entityTypeString(){
        return ChatColor.GREEN + Arrays.stream(EntityType.values()).map(EntityType::name).collect(Collectors.joining(", "));
    }

    public EntityType getType(ItemStack item){
        return EntityType.valueOf(ItemData.getString(item, ENTITY_TYPE_KEY));
    }

    public ItemStack createMinionEgg(ItemStack item, EntityType type){
        return ItemData.setValue(item, ENTITY_TYPE_KEY, type.name());
    }
}