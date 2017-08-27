package me.tlwv2.kitsp;

import com.google.common.collect.Maps;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.kitsp.commands.KitsPlusCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Map.Entry;

public class Main extends JavaPlugin {
    public static Map<String, Kit> kits = Maps.newHashMap();
    public static Main self;

    static final String KITSMAP = "kitsmap";
    static final String NPKEY = "needsPermissions";

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
}
