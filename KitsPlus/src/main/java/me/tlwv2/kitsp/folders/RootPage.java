package me.tlwv2.kitsp.folders;

import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RootPage extends Page implements ConfigurationSerializable, Cloneable{
    public static final String PAGE_SLOTS_KEY = "pages";
    public static final String ROOT_NAME = "Select Folder";

    public RootPage() {
        super(ROOT_NAME);
    }

    public RootPage(Inventory finalInventory){
        this();
        ItemStack[] contents = finalInventory.getContents();
        for(int i = 0; i < 45; i++){
            if(contents[i] != null) {
                if(!contents[i].hasItemMeta()){
                    continue;
                }
                String name = ChatColor.stripColor(contents[i].getItemMeta().getDisplayName());

                if(Main.instance().getGui().pageExists(name)){
                    slots.put(i, name);
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

    @Override
    public Inventory generateInventory(boolean edit){
        Inventory inv = Bukkit.createInventory(null, 54, name);
        for(int i = 0; i < 45; i++){
            String pageName = slots.get(i);
            if(edit){
                if(pageName != null){
                    inv.setItem(i, Main.instance().getGui().getPage(pageName).getIcon());
                }
                else{
                    inv.setItem(i, GuiUtils.insertFolderStack.clone());
                }
            }
            else if(pageName != null){
                inv.setItem(i, Main.instance().getGui().getPage(pageName).getIcon());
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

    public RootPage(Map<String, Object> map){
        super(ROOT_NAME);
        slots = (HashMap<Integer, String>) map.get(PAGE_SLOTS_KEY);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(PAGE_SLOTS_KEY, slots);

        return map;
    }
}
