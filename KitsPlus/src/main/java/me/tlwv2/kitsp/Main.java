package me.tlwv2.kitsp;

import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.utils.ItemData;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.kitsp.commands.KitsPlusCommand;
import me.tlwv2.kitsp.folders.GuiSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map.Entry;

public class Main extends JavaPlugin {
    //Constants
    private static final String KITS_MAP_KEY = "kitsmap";
    private static final String NO_PERMISSIONS_KEY = "needsPermissions";
    public static final String GUI_SETUP_KEY = "guiSetup";

    public static final String SIGN_TEXT_BEFORE = "kitsp sign";
    public static final String SIGN_TEXT_AFTER = ChatColor.DARK_BLUE + "[Kits]";
    public static final String KITS_PLUS_LOGO = ChatColor.YELLOW + "[" + ChatColor.GREEN + "KitsPlus" + ChatColor.YELLOW + "] ";
    public static final String KITS_PLUS_GUI_NAME = "Kits";

    public static final String PLACE_SIGN_PERM = "addon.kitsplus.placekitspsign";

    //Static Def
    private static Main self;

    //Instance Vars
    private HashMap<String, Kit> kits = new HashMap<>();
    private HashMap<Player, Kit> currentKits = new HashMap<>();
    private GuiSetup gui = new GuiSetup();

    private boolean needsPerms = true;
    private ItemStack kitRefill = null;

    @Override
    public void onEnable(){
        ConfigurationSerialization.registerClass(Kit.class);

        if(getConfig().contains(KITS_MAP_KEY)){
            for(Entry<String, Object> entry : getConfig().getConfigurationSection(KITS_MAP_KEY).getValues(false).entrySet()){
                kits.put(entry.getKey(), (Kit) entry.getValue());
            }
        }

        if(getConfig().contains(GUI_SETUP_KEY)){
            gui = (GuiSetup) getConfig().get(GUI_SETUP_KEY);
        }

        needsPerms = getConfig().getBoolean(NO_PERMISSIONS_KEY, true);

        self = this;
        ILWrapper.registerPlugin(self);
        new EListener(this);
        Bukkit.getPluginCommand("kitsplus").setExecutor(new KitsPlusCommand());

        kitRefill = new ItemStack(Material.SNOW_BALL);
        kitRefill = ItemUtil.addMetadata(kitRefill, "\u00a7bKit Refill", true);
        kitRefill = ItemData.setFlag(kitRefill, "kit_refill");
    }

    @Override
    public void onDisable(){
        getConfig().createSection(KITS_MAP_KEY, kits);
        getConfig().set(NO_PERMISSIONS_KEY, needsPerms);
        saveConfig();
    }

    /*
     * Checks if itemstack is kit icon
     */
    public Kit check(ItemStack i){
        for(Kit k : kits.values()){
            if(k.is(i))
                return k;
        }

        return null;
    }

    public static Main instance(){
        return self;
    }

    public HashMap<Player, Kit> getCurrentKits(){
        return currentKits;
    }

    public HashMap<String, Kit> getKits(){
        return kits;
    }

    public void addCurrentKit(Player p, Kit k){
        currentKits.put(p, k);
    }

    public Kit getKit(Player p){
        return currentKits.get(p);
    }

    public boolean hasKit(Player p){
        return currentKits.containsKey(p);
    }

    public void removeKit(Player p){
        currentKits.remove(p);
    }

    public ItemStack getRefillItemStack(){
        return kitRefill.clone();
    }

    public Kit getKit(String s){
        return kits.get(s);
    }

    public boolean hasKit(String s){
        return kits.containsKey(s);
    }

    public GuiSetup getGui() {
        return gui;
    }
}
