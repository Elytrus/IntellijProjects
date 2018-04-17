package me.tlwv2.admintool;

import me.tlwv2.admintool.commands.*;
import me.tlwv2.admintool.customrules.Rule;
import me.tlwv2.admintool.customrules.RuleTable;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AdminTool extends JavaPlugin {
    public static final String askMSG = ".  Please report what happened to @Plasmatic#2741 and what you did to trigger it";

    public static final String NOTE = ChatColor.BOLD + "" + ChatColor.YELLOW + "[" + ChatColor.RED + "!" + ChatColor.YELLOW + "] ";
    public static final String WARN = ChatColor.BOLD + "" + ChatColor.DARK_RED + "[!] ";
    public static final String GOOD = ChatColor.BOLD + "" + ChatColor.GREEN + "[!] ";
    public static final String NOPERM = WARN + "You do not have the permission to do that!";
    public static final String USAGE = WARN + "Usage: ";
    public static final String NOTPLAYER = WARN + "A Player must execute this command!";

    private static final String RULETABLE_KEY = "ruletable";
    public static AdminTool self;

    private RuleTable ruletable;
    private PermissionsEx permissions;

    @Override
    public void onLoad(){
        ConfigurationSerialization.registerClass(Rule.class);
        ConfigurationSerialization.registerClass(RuleTable.class);
    }

    @Override
    public void onEnable(){
        self = this;
        ruletable = (RuleTable) getConfig().get(RULETABLE_KEY, new RuleTable());

        ILWrapper.registerPlugin(this);

        Bukkit.getPluginCommand("rename").setExecutor(new RenameCommand());
        Bukkit.getPluginCommand("customrules").setExecutor(new CustomRulesCommand());
        Bukkit.getPluginCommand("attributes").setExecutor(new AttributesCommand());
        Bukkit.getPluginCommand("setstacksize").setExecutor(new StackSizeCommand());
        Bukkit.getPluginCommand("getperms").setExecutor(new GetPermsCommand());
        Bukkit.getPluginCommand("addpotioneffect").setExecutor(new SetPotionEffectsCommand());
        Bukkit.getPluginCommand("glow").setExecutor(new GlowCommand());

        new PluginEventListener();
    }

    @Override
    public void onDisable(){
        getConfig().set(RULETABLE_KEY, ruletable);
        saveConfig();
    }

    public static AdminTool instance(){
        return self;
    }

    public RuleTable table(){
        return ruletable;
    }

    public boolean vaultExists(){
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public PermissionsEx getPermissions(){
        return permissions;
    }
}
