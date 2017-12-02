package me.tlwv2.admintool.commands;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.core.utils.ItemData;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Moses on 2017-10-08.
 */
public class SetPotionEffectsCommand extends PlayerOnlyCommand {

    public static final String PERM = "addon.use.addpotioneffect";

    public SetPotionEffectsCommand() {
        ILWrapper.addCmd("addpotioneffect", "Sets the potion metadata for the currently held item", AdminTool.self);
        ILWrapper.addPerm(PERM, "Allows use of /addpotioneffect", AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        try{
            String id = arg3[0];
            int duration = Integer.parseInt(arg3[1]), amplifier = Integer.parseInt(arg3[2]);

            if(duration > 1000000 || amplifier > 127 || amplifier < -127)
                throw new IllegalArgumentException("Should be catched");

            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();

            if(meta instanceof PotionMeta){
                PotionMeta potionMeta = (PotionMeta)meta;
                PotionEffectType eff = PotionEffectType.getByName(id);
                if(eff == null){
                    p.sendMessage(Constants.WARN + "Invalid potion type! Here are the valid types");
                    p.sendMessage("\u00a7e--[Effects]--");
                    p.sendMessage("\u00a7e" + Arrays.stream(PotionEffectType.values()).filter(e -> e != null)
                            .map(PotionEffectType::getName)
                            .collect(Collectors.joining(", ")));
                    return true;
                }

                potionMeta.addCustomEffect(new PotionEffect(eff, duration, amplifier), true);
                item.setItemMeta(potionMeta);
            }
            else{
                p.sendMessage(Constants.WARN + "Invalid Item!");
                return true;
            }

            p.getInventory().setItemInMainHand(item);
        }
        catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
