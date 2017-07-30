package me.tlwv2.skmcplugin;

import com.earth2me.essentials.Essentials;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.skmcplugin.command.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main extends JavaPlugin{
    public static PlayerTimer afkTimer = new PlayerTimer(6000);
    public static PlayerTimer kittyCannonTimer = new PlayerTimer(6000, 3);
    public static PlayerTimer lightningTimer = new PlayerTimer(6000, 3);
    public static Map<String, String> chatColours = new HashMap<String, String>();
    public static Map<String, String> nickColours = new HashMap<String, String>();
    public static Map<String, String> chatFormats = new HashMap<String, String>();
    public static Map<String, String> nickFormats = new HashMap<String, String>();
    //public static Map<String, String> cPrefixes = new HashMap<String, String>();

    public static List<ItemStack> nickInv = new ArrayList<ItemStack>();
    public static List<ItemStack> chatInv = new ArrayList<ItemStack>();

    public static ItemStack cnPrefix = new ItemStack(Material.NAME_TAG, 1);
    public static ItemStack noPrefixes = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
    public static ItemStack noPerm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

    public static ItemStack openNickInv = new ItemStack(Material.SUGAR, 1);
    public static ItemStack openChatInv = new ItemStack(Material.GLOWSTONE_DUST, 1);

    public static ItemStack noColour = new ItemStack(Material.REDSTONE_BLOCK, 1);
    public static ItemStack noPrefix = new ItemStack(Material.REDSTONE_BLOCK, 1);

    public static ItemStack custoItemStack = null;

    public static List<Prefix> prefixes = null;

    public static Scoreboard nickCs = null;
    public static Essentials essentials = null;
    //public static CustomRuleTable ruleTable;

    public static boolean save = true;

    public Main(){

    }

    public static BukkitRunnable timerTimer = new BukkitRunnable() {

        @Override
        public void run() {
            afkTimer.update();
            kittyCannonTimer.update();
            lightningTimer.update();
        }
    };

    public void onLoad(){
        ConfigurationSerialization.registerClass(Prefix.class);
        ConfigurationSerialization.registerClass(PlayerTimer.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable(){
        nickCs = Bukkit.getScoreboardManager().getMainScoreboard();
        //in case lol
        ConfigurationSerialization.registerClass(Prefix.class);
        ConfigurationSerialization.registerClass(PlayerTimer.class);

        new EListener(this);
        ILWrapper.registerPlugin(this);

		/*if(!getServer().getPluginManager().isPluginEnabled("Essentials")){
			getLogger().severe("Essentials not detected!");
			getLogger().severe("Plugin will not function properly without Essentials!");
		}
		else
			getLogger().finest("Essentials detected!");

		if(!getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			getLogger().severe("PermissionsEx not detected!");
			getLogger().severe("Plugin will not function properly without PermissionsEx!");
		}
		else
			getLogger().finest("PermissionsEx detected!");*/

        saveConfig();

        prefixes = (List<Prefix>) getConfig().getList("prefixes", new ArrayList<Prefix>());
        custoItemStack = getConfig().getItemStack("custoInvItemstack", null);
        save = getConfig().getBoolean("save", true);

        if(save){
            if(getConfig().contains("afktimer")){
                afkTimer = (PlayerTimer) getConfig().get("afktimer");
                kittyCannonTimer = (PlayerTimer) getConfig().get("kittycannontimer");
                lightningTimer = (PlayerTimer) getConfig().get("lightningtimer");
            }

            if(getConfig().contains("chatcolours"))
                for(Entry<String, Object> e : getConfig().getConfigurationSection("chatcolours").getValues(true).entrySet())
                    chatColours.put(e.getKey(), e.getValue().toString());

            if(getConfig().contains("nickcolours"))
                for(Entry<String, Object> e : getConfig().getConfigurationSection("nickcolours").getValues(true).entrySet())
                    nickColours.put(e.getKey(), e.getValue().toString());

            if(getConfig().contains("chatformats"))
                for(Entry<String, Object> e : getConfig().getConfigurationSection("chatformats").getValues(true).entrySet())
                    chatFormats.put(e.getKey(), e.getValue().toString());

            if(getConfig().contains("nickformats"))
                for(Entry<String, Object> e : getConfig().getConfigurationSection("nickformats").getValues(true).entrySet())
                    nickFormats.put(e.getKey(), e.getValue().toString());

			/*if(getConfig().contains("cprefixes"))
				for(Entry<String, Object> e : getConfig().getConfigurationSection("cprefixes").getValues(true).entrySet())
					cPrefixes.put(e.getKey(), e.getValue().toString());*/
        }

        ItemUtil.addMetadata(cnPrefix, ChatColor.YELLOW + "Change Prefix", true);
        ItemUtil.addMetadata(noPrefixes, ChatColor.RED + "No prefixes available!", true);

        ItemUtil.addMetadata(noColour, ChatColor.RED + "Remove Colour", true);
        ItemUtil.addMetadata(noPrefix, ChatColor.RED + "Remove Prefix", true);

        ItemUtil.addMetadata(noPerm, ChatColor.RED + "No Permission!", true);

        ItemUtil.addMetadata(openNickInv, ChatColor.BLUE + "Change Nick Colour", true);
        ItemUtil.addMetadata(openChatInv, ChatColor.BLUE + "Change Chat Colour", true);

        for(Field f : Arrays.stream(ChatColor.class.getFields())
                .filter(e -> e.getType().isEnum())
                .collect(Collectors.toList())){

            ChatColor cc = null;
            try {
                cc = (ChatColor) f.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            ItemStack it = new ItemStack(Material.SUGAR, 1);
            ItemStack itt = new ItemStack(Material.GLOWSTONE_DUST, 1);

            String name = Arrays.stream(f.getName()
                    .toLowerCase()
                    .replaceAll("_", " ")
                    .split(" "))
                    .map(e -> e.substring(0, 1).toUpperCase() + e.substring(1, e.length()))
                    .collect(Collectors.joining(" "));

            ItemUtil.addMetadata(it, cc + name, true);
            ItemUtil.addMetadata(itt, cc + name, true);

            if(isFormat(cc)){
                it.setType(Material.REDSTONE);
                itt.setType(Material.SULPHUR);
            }

            nickInv.add(it);
            chatInv.add(itt);
        }

        timerTimer.runTaskTimer(this, 0, 1);

		/*nickColours.entrySet().stream()
		.forEach(e -> {
			if(Bukkit.getServer().getOfflinePlayer(UUID.fromString(e.getKey())).isOnline()){
				Player p = Bukkit.getServer().getPlayer(UUID.fromString(e.getKey()));

				p.setDisplayName(e.getValue().replaceAll("null", "") + p.getDisplayName() + ChatColor.RESET);
			}
		});*/

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            essentials = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
            //ruleTable = (CustomRuleTable) getConfig().get("ruleTable", new CustomRuleTable(this));
            //getServer().getOnlinePlayers().forEach(p -> EListener.updateUsername(p));
        });

        //COMMANDS

        Bukkit.getPluginCommand("bam").setExecutor(new BamCommand());
        Bukkit.getPluginCommand("prefix").setExecutor(new PrefixCommand());
        Bukkit.getPluginCommand("togglesave").setExecutor(new ToggleSaveCommand());
        Bukkit.getPluginCommand("clean").setExecutor(new CleanCommand());
        Bukkit.getPluginCommand("customization").setExecutor(new CustomizationCommand());
    }

    @Override
    public void onDisable(){
        getConfig().set("prefixes", prefixes);
        getConfig().set("custoInvItemstack", custoItemStack);
        getConfig().set("save", save);
        //getConfig().set("ruleTable", ruleTable);

        if(save){
            getConfig().set("afktimer", afkTimer);
            getConfig().set("kittycannontimer", kittyCannonTimer);
            getConfig().set("lightningtimer", lightningTimer);
            getConfig().createSection("chatcolours", chatColours);
            getConfig().createSection("nickcolours", nickColours);
            getConfig().createSection("chatformats", chatFormats);
            getConfig().createSection("nickformats", nickFormats);
            //getConfig().createSection("nickformats", cPrefixes);
        }

        for(Player p : getServer().getOnlinePlayers()){
            String name = p.getName();

            if(Main.nickCs.getTeam(name) != null)
                Main.nickCs.getTeam(name).unregister();
        }

        saveConfig();

        getLogger().info(getConfig().getCurrentPath());
    }

    public static Inventory nci(){
        Inventory inv = Bukkit.createInventory(null, 27, "Nick Colour");

        inv.addItem(nickInv.toArray(new ItemStack[0]));
        inv.setItem(26, noColour);

        return inv;
    }

    public static Inventory cci(){
        Inventory inv = Bukkit.createInventory(null, 27, "Chat Colour");

        inv.addItem(chatInv.toArray(new ItemStack[0]));
        inv.setItem(26, noColour);

        return inv;
    }

    public static Inventory cmi(){
        Inventory inv = Bukkit.createInventory(null, 9, "Customization");

        inv.setItem(2, openNickInv);
        inv.setItem(3, openChatInv);
        inv.setItem(5, cnPrefix);

        return inv;
    }

    public static boolean isFormat(ChatColor c){
        return ChatColor.BOLD == c || ChatColor.ITALIC == c || ChatColor.MAGIC == c
                || ChatColor.RESET == c || ChatColor.STRIKETHROUGH == c || ChatColor.UNDERLINE == c;
    }

    public static Prefix getByName(String name){
        for(Prefix p : prefixes)
            if(p.equals(name))
                return p;

        return null;
    }

    public static Prefix getByRep(ItemStack rep){
        for(Prefix p : prefixes)
            if(p.isSame(rep))
                return p;

        return null;
    }
}