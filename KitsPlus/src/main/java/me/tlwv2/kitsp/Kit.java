package me.tlwv2.kitsp;

import com.google.common.collect.Maps;
import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Map;

public class Kit implements ConfigurationSerializable, Cloneable {
    static final String CONTENTSKEY = "kitinventorycontents";
    static final String EFFECTSKEY = "kitpotioneffects";
    static final String ICONKEY = "kitinventoryselectionicon";

    private ItemStack[] contents;
    private PotionEffect[] effects;
    private ItemStack icon;
    private String name;

    public Kit(Player p, String name, String displayName){
        reset(p, name, displayName);
    }

    /////////////////////////////////////////////////////////////////////////////

    public void apply(Player p){
        applyWithoutHP(p);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.self, () -> {
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }, 5l);
    }

    public void applyWithoutHP(Player p){
        p.getInventory().setContents(contents.clone());

        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }

        for(PotionEffect pe : effects){
            if(pe == null)
                continue;
            p.addPotionEffect(pe);
        }
    }

    public void reset(Player p, String name, String displayName){
        contents = p.getInventory().getContents().clone();
        effects = p.getActivePotionEffects().toArray(new PotionEffect[1]);

        ItemStack i = p.getInventory().getItemInMainHand().clone();
        ItemStack defaulticon = new ItemStack(Material.STONE, 1);

        defaulticon = ItemUtil.addMetadata(defaulticon, ChatColor.RESET + name, false);
        i = ItemUtil.addMetadata(i, ChatColor.translateAlternateColorCodes('&', displayName), false);

        if(i.equals(null))
            icon = defaulticon;
        else if(i.getType() == Material.AIR)
            icon = defaulticon;
        else
            icon = i;

        this.name = name;
    }

    public boolean is(ItemStack i){
        return icon.equals(i);
    }

    /////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    public Kit(Map<String, Object> map){
        contents = ((ArrayList<ItemStack>) map.get(CONTENTSKEY)).toArray(new ItemStack[0]);
        effects = ((ArrayList<PotionEffect>) map.get(EFFECTSKEY)).toArray(new PotionEffect[0]);
        icon = (ItemStack) map.get(ICONKEY);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        map.put(CONTENTSKEY, contents);
        map.put(EFFECTSKEY, effects);
        map.put(ICONKEY, icon);

        return map;
    }

    public Object clone(){
        return this.clone();
    }

    //////////////////////////////////////////////////////////////////////////////
    public void setEffects(Player p) {
        this.effects = p.getActivePotionEffects().toArray(new PotionEffect[1]);
    }

    public void setContents(Player p) {
        this.contents = p.getInventory().getContents().clone();
    }

    public void setIcon(Player p) {
        this.icon = p.getInventory().getItemInMainHand();
    }

    public void setIcon(ItemStack i){
        this.icon = i;
    }

    public ItemStack getIcon(){
        return icon;
    }

    public String getName(){
        return name;
    }
}
