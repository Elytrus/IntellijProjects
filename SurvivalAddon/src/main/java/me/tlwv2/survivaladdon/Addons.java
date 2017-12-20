package me.tlwv2.survivaladdon;


import me.tlwv2.survivaladdon.addons.EntityKillManager;
import me.tlwv2.survivaladdon.addons.PlayTimeManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Addons extends JavaPlugin {
    //DEFAULTS
    private static final int DEFAULT_LEVEL_OFFSET = 400;
    private static final int DEFAULT_LEVEL_INCREMENT = 50;

    private static final int DEFAULT_PLAY_TIME_INCREMENT = 25;
    private static final long DEFAULT_PLAY_TIME_DELAY = 1200L;

    private static final String[] DEFAULT_ENTITY_KILL_ENTITIES = {
            Zombie.class.getSimpleName(),
            Skeleton.class.getSimpleName(),
            Creeper.class.getSimpleName(),
            Spider.class.getSimpleName(),
            Enderman.class.getSimpleName(),
            Wither.class.getSimpleName(),
            EnderDragon.class.getSimpleName()
    };
    private static final int[] DEFAULT_ENTITY_KILL_REWARDS = {
            100,
            85,
            150,
            70,
            350,
            1250,
            2500
    };

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "admin";

    //CONFIGURATION KEYS
    private static final String ENTITY_KILLS_MANAGER_KEY = "entityKills";
    private static final String PLAY_TIME_MANAGER_KEY = "playTime";
    private static final String LEVEL_OFFSET_KEY = "levelOffset";
    private static final String LEVEL_INCREMENT_KEY = "levelIncrement";
    private static final String HOST_KEY = "dbHost";
    private static final String USERNAME_KEY = "dbUsername";
    private static final String PASSWORD_KEY = "dbPassword";

    //MISC CONSTANTS
    public static final String PLUGIN_NAME = "PointAddon";

    //VARIABLES
    private static Addons instance;

    private LevelManager manager;
    private DisplayManager displayManager;

    private PlayTimeManager playTimeManager;
    private ArrayList<EntityKillManager> entityKillManagers;

    private int levelOffset;
    private int levelIncrement;

    private String host;
    private String username;
    private String password;

    @Override
    public void onDisable() {
        this.manager.update();

        getConfig().set(ENTITY_KILLS_MANAGER_KEY, this.entityKillManagers);
        getConfig().set(PLAY_TIME_MANAGER_KEY, this.playTimeManager);
        getConfig().set(LEVEL_OFFSET_KEY, this.levelOffset);
        getConfig().set(LEVEL_INCREMENT_KEY, this.levelIncrement);
        getConfig().set(HOST_KEY, this.host);
        getConfig().set(USERNAME_KEY, this.username);
        getConfig().set(PASSWORD_KEY, this.password);
    }

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(EntityKillManager.class);
        ConfigurationSerialization.registerClass(PlayTimeManager.class);
    }

    @Override
    public void onEnable() {
        this.displayManager = new DisplayManager();
        this.entityKillManagers = new ArrayList<>();

        //MANAGER KEYS
        if(getConfig().contains(PLAY_TIME_MANAGER_KEY, true)){
            this.playTimeManager = (PlayTimeManager) getConfig().get(PLAY_TIME_MANAGER_KEY);
        }
        else{
            this.playTimeManager = new PlayTimeManager(DEFAULT_PLAY_TIME_INCREMENT, DEFAULT_PLAY_TIME_DELAY, this);
            getConfig().set(PLAY_TIME_MANAGER_KEY, this.playTimeManager);
        }

        if(getConfig().contains(ENTITY_KILLS_MANAGER_KEY)){
            for (Object killManager : getConfig().getList(ENTITY_KILLS_MANAGER_KEY)) {
                this.entityKillManagers.add((EntityKillManager) killManager);
            }
        }
        else{
            if(DEFAULT_ENTITY_KILL_ENTITIES.length != DEFAULT_ENTITY_KILL_REWARDS.length){
                getLogger().severe("Default entity kill lists do not match! Is there something wrong with the source code?");
                getLogger().severe("Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            else {
                for(int i = 0; i < DEFAULT_ENTITY_KILL_REWARDS.length; i++){
                    this.entityKillManagers.add(new EntityKillManager(DEFAULT_ENTITY_KILL_ENTITIES[i], DEFAULT_ENTITY_KILL_REWARDS[i], this));
                }
                getConfig().set(ENTITY_KILLS_MANAGER_KEY, this.entityKillManagers);
            }
        }

        //OFFSET KEYS
        if(getConfig().contains(LEVEL_OFFSET_KEY)){
            this.levelOffset = getConfig().getInt(LEVEL_OFFSET_KEY);
        }
        else{
            this.levelOffset = DEFAULT_LEVEL_OFFSET;
            getConfig().set(LEVEL_OFFSET_KEY, this.levelOffset);
        }

        if(getConfig().contains(LEVEL_INCREMENT_KEY)){
            this.levelIncrement = getConfig().getInt(LEVEL_INCREMENT_KEY);
        }
        else{
            this.levelIncrement = DEFAULT_LEVEL_INCREMENT;
            getConfig().set(LEVEL_INCREMENT_KEY, this.levelIncrement);
        }

        //SQL KEYS
        if(getConfig().contains(HOST_KEY)){
            this.host = getConfig().getString(HOST_KEY);
        }
        else{
            this.host = DEFAULT_HOST;
            getConfig().set(HOST_KEY, this.host);
        }

        if(getConfig().contains(USERNAME_KEY)){
            this.username = getConfig().getString(USERNAME_KEY);
        }
        else{
            this.username = DEFAULT_USERNAME;
            getConfig().set(USERNAME_KEY, this.username);
        }

        if(getConfig().contains(PASSWORD_KEY)){
            this.password = getConfig().getString(PASSWORD_KEY);
        }
        else{
            this.password = DEFAULT_PASSWORD;
            getConfig().set(PASSWORD_KEY, this.password);
        }

        //OTHER

        this.manager = new LevelManager(host, username, password);
        instance = this;
    }

    public static Addons getInstance(){
        return instance;
    }

    public int getLevel(int points){
        int off = points - levelOffset;
        if(off < 0){
            return 1;
        }

        return off / levelIncrement + 1;
    }

    public void checkLevel(String uuid){
        int level = manager.getLevel(uuid), points = manager.getPoints(uuid);
        int newLevel = getLevel(points);

        if(newLevel != level){
            manager.setLevel(uuid, newLevel);
        }
    }

    public LevelManager manager(){
        return manager;
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public PlayTimeManager getPlayTimeManager() {
        return playTimeManager;
    }

    public ArrayList<EntityKillManager> getEntityKillManagers() {
        return entityKillManagers;
    }
}
