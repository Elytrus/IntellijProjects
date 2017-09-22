package me.tlwv2.oruledisplay;

import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;

public class ObtrusiveRuleDisplay extends JavaPlugin{
    public static final String BYPASS_PERM = "addon.bypass.oruledisplay";
    public static ObtrusiveRuleDisplay self;

    private int fadeIn;
    private int stay;
    private int fadeOut;
    private List<String> rules;
    private String prefix;

    @Override
    public void onEnable() {
        self = this;

        if(!new File("plugins/ObtrusiveRuleDisplay/config.yml").exists()){
            getLogger().severe("Config does not exist! Creating new config...");
            resetConfig();
        }

        fadeIn = getConfigInt("fadeIn");
        stay = getConfigInt("stay");
        fadeOut = getConfigInt("fadeOut");
        rules = getConfigList("rules");
        prefix = getConfigString("prefix");

//        getConfig().set("fadeIn", fadeIn);
//        getConfig().set("stay", stay);
//        getConfig().set("fadeOut", fadeOut);
//        getConfig().set("rules", rules);
//        getConfig().set("prefix", prefix);
//
//        saveConfig();

        ILWrapper.registerPlugin(self);
        ILWrapper.addPerm(BYPASS_PERM, "Allows you to bypass the Rule Display when you join the game", self);

        new EListener(self);
    }

    @Override
    public void onDisable() {

    }

    private int getConfigInt(String key) {
        if (getConfig().contains(key)) {
            int value = getConfig().getInt(key);
            getLogger().info("Found Integer " + key + " with value " + value);

            return value;
        }

        resetConfig();

        getLogger().severe("\u00a7cInvalid configuration structure: Could not find value " + key
                + "! Resetting Config");
        
        return getConfig().getInt(key);
    }

    private void resetConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
    }

    private List<String> getConfigList(String key) {
        if (getConfig().contains(key)) {
            List<String> list = getConfig().getStringList(key);
            getLogger().info("Found List " + key + " with value " + list);
            return list;
        }

        resetConfig();

        getLogger().severe("\u00a7cInvalid configuration structure: Could not find value " + key
                + "! Resetting Config");

        return getConfig().getStringList(key);
    }

    private String getConfigString(String key) {
        if (getConfig().contains(key)) {
            String value = getConfig().getString(key);
            getLogger().info("Found String " + key + " with value " + value);

            return value;
        }

        resetConfig();

        getLogger().severe("\u00a7cInvalid configuration structure: Could not find value " + key
                + "! Resetting Config");

        return getConfig().getString(key);
    }

    public void printForPlayer(Player p){
        Bukkit.getPluginManager().callEvent(new RuleSendEvent(p));
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public List<String> getRules() {
        return rules;
    }

    public String getPrefix() {
        return prefix;
    }
}
