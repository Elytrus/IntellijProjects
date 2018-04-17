package me.tlwv2.kitsp;

import com.google.common.collect.Maps;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.utils.ItemData;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.kitsp.commands.KitsPlusCommand;
import net.minecraft.server.v1_12_R1.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main extends JavaPlugin {
    private static HashMap<String, Kit> kits = new HashMap<>();
    private static HashMap<Player, Kit> currentKits = new HashMap<>();
    public static Main self;

    private static final String KITSMAP = "kitsmap";
    private static final String NPKEY = "needsPermissions";
    private static ItemStack kitRefill = null;

    public static final String SIGNTEXTBEFORE = "kitsp sign";
    public static final String SIGNTEXTAFTER = ChatColor.DARK_BLUE + "[Kits]";

    public static final String KITSPLOGO = ChatColor.YELLOW + "[" + ChatColor.GREEN + "KitsPlus" + ChatColor.YELLOW + "] ";
    public static final String KITSPGUINAME = "Kits";

    public static boolean needsPerms = true;

    @Override
    public void onEnable(){
        ConfigurationSerialization.registerClass(Kit.class);

        if(getConfig().contains(KITSMAP)){
            for(Entry<String, Object> entry : getConfig().getConfigurationSection(KITSMAP).getValues(false).entrySet()){
                kits.put(entry.getKey(), (Kit) entry.getValue());
            }
        }

        needsPerms = getConfig().getBoolean(NPKEY, true);

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
        getConfig().createSection(KITSMAP, kits);
        getConfig().set(NPKEY, needsPerms);
        saveConfig();
    }

    /*
     * Checks if itemstack is kit icon
     */
    public static Kit check(ItemStack i){
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
}
