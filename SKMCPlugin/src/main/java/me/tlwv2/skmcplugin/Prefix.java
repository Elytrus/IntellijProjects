package me.tlwv2.skmcplugin;

import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Prefix implements ConfigurationSerializable {
    private String name;
    private String prefix;
    private ItemStack rep;

    public Prefix(String name, String prefix, ItemStack rep) {
        this.name = name;
        this.prefix = prefix;
        setRep(rep);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("name", name);
        map.put("prefix", prefix);
        map.put("rep", rep);

        return map;
    }

    public static Prefix deserialize(Map<String, Object> map){
        return new Prefix((String)map.get("name"), (String)map.get("prefix"), (ItemStack)map.get("rep"));
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Prefix)) return false;

        return ((Prefix)o).getName().equals(getName());
    }

    public boolean equals(String o){
        return getName().equals(o);
    }

    public String getPermission(){
        return "addon.prefixes." + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ItemStack getRep() {
        return rep;
    }

    public void setRep(ItemStack rep) {
        this.rep = rep;

        ItemUtil.addMetadata(rep, prefix, true, ChatColor.RESET + "Changes prefix to " + prefix);
    }

    public boolean isSame(ItemStack other){
        return this.rep.equals(other);
    }
}
