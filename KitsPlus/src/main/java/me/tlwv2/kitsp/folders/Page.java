package me.tlwv2.kitsp.folders;

import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.kitsp.Kit;
import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Page implements ConfigurationSerializable, Cloneable {
    public static final String DATA_KEY = "pageData";
    public static final String NAME_KEY = "name";
    protected HashMap<Integer, String> slots;
    private ItemStack icon;
    protected String name;

    public Page(String name) {
        this.slots = new HashMap<>();
        this.name = name;

        if(name.equals(RootPage.ROOT_NAME)){
            this.icon = null;
        }
        else{
            this.icon = ItemUtil.addMetadata(new ItemStack(Material.EMPTY_MAP), name, true);
        }
    }

    public Page(Inventory finalInventory, String name){
        this(name);
        ItemStack[] contents = finalInventory.getContents();
        for(int i = 0; i < 45; i++){
            if(contents[i] != null) {
                Kit k = Main.instance().check(contents[i]);
                if (k != null) {
                    slots.put(i, k.getName());
                }
                else{
                    slots.remove(i);
                }
            }
            else{
                slots.remove(i);
            }
        }
    }

    public Page(Map<String, Object> map){
        this.slots = (HashMap<Integer, String>) map.get(DATA_KEY);
        this.name = (String) map.get(NAME_KEY);
    }

    public Inventory generateInventory(boolean edit){
        Inventory inv = Bukkit.createInventory(null, 54, name);
        for(int i = 0; i < 45; i++){
            String kitName = slots.get(i);
            if(edit){
                if(kitName != null){
                    inv.setItem(i, Main.instance().getKit(kitName).getIcon());
                }
                else{
                    inv.setItem(i, GuiUtils.insertKitStack.clone());
                }
            }
            else if(kitName != null){
                inv.setItem(i, Main.instance().getKit(kitName).getIcon());
            }
        }

        for(int i = 0; i < 9; i++){
            if(i == 4){
                inv.setItem(i + 45, GuiUtils.exitStack.clone());
            }
            else{
                inv.setItem(i + 45, GuiUtils.invalidStack.clone());
            }
        }

        return inv;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String forSlot(int slot){
        return slots.get(slot);
    }

    public boolean slotExists(int slot){
        return forSlot(slot) != null;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DATA_KEY, slots);
        map.put(NAME_KEY, name);
        return map;
    }
}
