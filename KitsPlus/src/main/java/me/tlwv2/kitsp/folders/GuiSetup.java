package me.tlwv2.kitsp.folders;

import me.tlwv2.kitsp.Kit;
import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuiSetup implements ConfigurationSerializable, Listener {
    public static final String LAYOUT_KEY = "layout";
    public static final String PAGES_KEY = "pages";
    public static final String ROOT_PAGE_NAME = "_root";

    private HashMap<Integer, String> pageLayout;
    private HashMap<String, Page> pages;
    private RootPage rootPage;
    private HashMap<Player, PageData> currentData;

    private Main main;

    //In edit mode, everything is insert kit/folder
    //Left click to add kit/folder
    //Right click to remove

    public GuiSetup() {
        this.pageLayout = new HashMap<>();
        this.pages = new HashMap<>();
        this.currentData = new HashMap<>();
        this.rootPage = new RootPage();

        this.main = Main.instance();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public GuiSetup(Map<String, Object> map){
        this.pageLayout = (HashMap<Integer, String>) map.get(LAYOUT_KEY);
        this.pages = (HashMap<String, Page>) map.get(PAGES_KEY);
        this.currentData = new HashMap<>();
    }

    public Page getPage(String name){
        if(name.equals(ROOT_PAGE_NAME)){
            return rootPage;
        }
        return pages.get(name);
    }

    public boolean pageExists(String name){
        return getPage(name) != null;
    }

    public void setPage(String name, Page page){
        pages.put(name, page);
    }

    public Collection<String> pageNames(){
        return pages.keySet();
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(LAYOUT_KEY, pageLayout);
        map.put(PAGES_KEY, pages);

        return map;
    }

    //oh god...
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        int slot = e.getSlot();

        if(!currentData.containsKey(player)){
            return;
        }

        if(item == null){
            return;
        }

        PageData data = currentData.get(player);
        if(data.getType() == PageData.PageType.ROOT){
            String pageName = rootPage.forSlot(slot);
            if(pageName != null){
                Page page = pages.get(pageName);
                if(page == null){
                    return;
                }
                currentData.put(player, new PageData(pageName));
                player.closeInventory();
                player.openInventory(page.generateInventory(false));
            }
        }
        else if(data.getType() == PageData.PageType.FOLDER_SELECT){
            Page currentPage = pages.get(data.getCurrentPage());
            String kitName = currentPage.forSlot(slot);
            if(kitName != null){
                Kit kit = Main.instance().getKit(kitName);
                player.closeInventory();
                kit.apply(player);
            }
        }
        else if(data.getType() == PageData.PageType.FOLDER_EDIT){
            ClickType clickType = e.getClick();
            if(clickType == ClickType.LEFT){
                //
            }
            else if(clickType == ClickType.RIGHT){
                //
            }
        }
    }
}
