package me.tlwv2.skmcplugin;

import com.earth2me.essentials.User;
import me.tlwv2.core.Constants;
import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EListener implements Listener{
    public static Main plugin;

    public EListener(Main plugin){
        EListener.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(this, EListener.plugin);
    }

    @EventHandler
    public void onComm(PlayerCommandPreprocessEvent e){
        String[] cmd = e.getMessage().substring(1).split(" ");
        Player p = e.getPlayer();

        if(cmd[0].equalsIgnoreCase("afk")){
            if(!Main.afkTimer.ready(p)){
                sendTimeMessage(p, Main.afkTimer.timeLeft(p), "/afk");
                e.setCancelled(true);
            }
            else{
                if(p.hasPermission("addon.bypasscooldown.afk"))
                    return;

                Main.afkTimer.use(p);
            }
        }
        else if(cmd[0].equalsIgnoreCase("kittycannon")){
            if(!Main.kittyCannonTimer.ready(p)){
                sendTimeMessage(p, Main.kittyCannonTimer.timeLeft(p), "/kittycannon");
                e.setCancelled(true);
            }
            else{
                if(p.hasPermission("addon.bypasscooldown.kittycannon"))
                    return;

                Main.kittyCannonTimer.use(p);

                p.sendMessage(Constants.NOTE + "You can use /kittycannon " + Main.kittyCannonTimer.attsLeft(p) + " more time(s) before going on cooldown!");
            }
        }
        else if(cmd[0].equalsIgnoreCase("bam")){
            if(!p.hasPermission("addon.use.safelightning"))
                return;

            if(!Main.lightningTimer.ready(p)){
                sendTimeMessage(p, Main.lightningTimer.timeLeft(p), "/bam");
                e.setCancelled(true);
            }
            else{
                if(p.hasPermission("addon.bypasscooldown.safelightning"))
                    return;

                Main.lightningTimer.use(p);

                p.sendMessage(Constants.NOTE + "You can use /bam " + Main.lightningTimer.attsLeft(p) + " more time(s) before going on cooldown!");
            }
        }
		/*else if(cmd[0].equalsIgnoreCase("nick")){
			String UUID = p.getUniqueId().toString();

			if(Main.nickColours.containsKey(UUID))
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					p.setDisplayName(Main.nickColours.get(UUID).replaceAll("null", "") + p.getDisplayName() + ChatColor.RESET);
				}, 10);
		}*/
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Inventory i = e.getInventory();

        if(e.getCurrentItem() == null)
            return;

        if(Main.custoItemStack != null)
            if((e.getInventory().getType() != InventoryType.PLAYER &&
                    e.getInventory().getType() != InventoryType.CRAFTING) &&
                    e.getCurrentItem().equals(Main.custoItemStack))
                e.setCancelled(true);

        if(i.getName().equals("Customization")){
            e.setCancelled(true);

            if(e.getSlot() == 2){
                e.getWhoClicked().openInventory(Main.nci());
            }
            else if(e.getSlot() == 3){
                e.getWhoClicked().openInventory(Main.cci());
            }
            else if(e.getSlot() == 5){
                List<Prefix> prefixes = Main.prefixes.stream()
                        .filter(p -> e.getWhoClicked().hasPermission(p.getPermission()))
                        .collect(Collectors.toList());

                Inventory prefixInv = null;

                if(prefixes.size() == 0){
                    prefixInv = Bukkit.createInventory(null, 9, "Prefixes");

                    for(int j = 0; j < prefixInv.getSize(); j++)
                        prefixInv.setItem(j, Main.noPrefixes);
                }
                else{
                    int rc = prefixes.size() / 9;

                    prefixInv = Bukkit.createInventory(null, rc * 9 + 9, "Prefixes");

                    prefixInv.addItem(prefixes.stream()
                            .map(p -> p.getRep())
                            .collect(Collectors.toList())
                            .toArray(new ItemStack[0]));

                    prefixInv.setItem(prefixInv.getSize() - 1, Main.noPrefix);
                }

                e.getWhoClicked().openInventory(prefixInv);
            }
        }
        else if(i.getName().equals("Prefixes")){
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            //String UUID = p.getUniqueId().toString();

            if(e.getCurrentItem().equals(Main.noPrefix)){
                ItemStack pre = new ItemStack(Material.EMPTY_MAP);
                ItemUtil.addMetadata(pre, "§rPrefix removed!", true);

                //Main.cPrefixes.remove(UUID);
                sendCommand(String.format("pex user %1s prefix \"\"", p.getName()));

                //updateUsername(p);
                previewInventory(e.getInventory(), pre, e.getSlot());
            }
            else if(e.getCurrentItem().equals(Main.noPrefixes))
                return;
            else{
                Prefix prefix = Main.getByRep(e.getCurrentItem());
                String name = p.getName();
                String cc = "";

                if(Main.nickColours.containsKey(name))
                    cc += Main.nickColours.get(name);
                if(Main.nickFormats.containsKey(name))
                    cc += Main.nickFormats.get(name);

                ItemStack pre = new ItemStack(Material.EMPTY_MAP);
                ItemUtil.addMetadata(pre, "§rPreview: " + prefix.getPrefix() + cc + getBaseName(p), true);

                //Main.cPrefixes.put(UUID, prefix.getPrefix());
                sendCommand(String.format("pex user %1s prefix \"%2s\"", p.getName(), prefix.getPrefix()));

                //updateUsername(p);
                previewInventory(e.getInventory(), pre, e.getSlot());
            }
        }
        else if(i.getName().equals("Nick Colour")){
            e.setCancelled(true);

            if(e.getCurrentItem().equals(Main.noColour)){
                Player p = (Player)e.getWhoClicked();
                String name = p.getName();

                Main.nickColours.remove(name);
                Main.nickFormats.remove(name);

                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ChatColor.RESET + "Preview: " + ChatColor.stripColor(getBaseName(p)), true);

                if(Main.nickCs.getTeam(name) != null)
                    Main.nickCs.getTeam(name).unregister();

                updateUsername(p);
                previewInventory(e.getInventory(), pre, e.getSlot());
            }
            else if(e.getCurrentItem().getType() == Material.SUGAR){
                Player p = (Player)e.getWhoClicked();

                if(!p.hasPermission("addon.nick.clr")){
                    previewInventory(e.getInventory(), Main.noPerm, e.getSlot());
                    return;
                }

                String d = e.getCurrentItem().getItemMeta().getDisplayName();
                String cc = d.replace(ChatColor.stripColor(d), "");
                String name = p.getName();

                Main.nickColours.put(name, cc);

                String ccp = (Main.nickColours.get(name) + Main.nickFormats.get(name)).replaceAll("null", "");

                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ChatColor.RESET + "Preview: " + ccp + ChatColor.stripColor(getBaseName(p)), true);

                updateUsername(p);
                previewInventory(e.getInventory(), pre, e.getSlot());
            }
            else if(e.getCurrentItem().getType() == Material.REDSTONE){
                Player p = (Player)e.getWhoClicked();

                if(!p.hasPermission("addon.nick.fmt")){
                    previewInventory(e.getInventory(), Main.noPerm, e.getSlot());
                    return;
                }

                String d = e.getCurrentItem().getItemMeta().getDisplayName();
                String cc = d.replace(ChatColor.stripColor(d), "");
                String name = p.getName();

                Main.nickFormats.put(name, cc);

                String ccp = (Main.nickColours.get(name) + Main.nickFormats.get(name)).replaceAll("null", "");

                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ChatColor.RESET + "Preview: " + ccp + ChatColor.stripColor(p.getDisplayName()), true);

                updateUsername(p);
                previewInventory(e.getInventory(), pre, e.getSlot());
            }
        }
        else if(i.getName().equals("Chat Colour")){
            e.setCancelled(true);

            if(e.getCurrentItem().equals(Main.noColour)){
                Player p = (Player)e.getWhoClicked();

                Main.chatColours.remove(p.getUniqueId().toString());
                Main.chatFormats.remove(p.getUniqueId().toString());

                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ChatColor.RESET + "Preview", true);

                previewInventory(e.getInventory(), pre, e.getSlot());
            }
            else if(e.getCurrentItem().getType() == Material.GLOWSTONE_DUST){
                Player p = (Player)e.getWhoClicked();

                if(!p.hasPermission("addon.chat.clr")){
                    previewInventory(e.getInventory(), Main.noPerm, e.getSlot());
                    return;
                }

                String d = e.getCurrentItem().getItemMeta().getDisplayName();
                String cc = d.replace(ChatColor.stripColor(d), "");
                String UUID = p.getUniqueId().toString();

                Main.chatColours.put(UUID, cc);

                String ccp = (Main.chatColours.get(UUID) + Main.chatFormats.get(UUID)).replaceAll("null", "");
                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ccp + "Preview", true);

                previewInventory(e.getInventory(), pre, e.getSlot());
            }
            else if(e.getCurrentItem().getType() == Material.SULPHUR){
                Player p = (Player)e.getWhoClicked();

                if(!p.hasPermission("addon.chat.fmt")){
                    previewInventory(e.getInventory(), Main.noPerm, e.getSlot());
                    return;
                }

                String d = e.getCurrentItem().getItemMeta().getDisplayName();
                String cc = d.replace(ChatColor.stripColor(d), "");
                String UUID = p.getUniqueId().toString();

                Main.chatFormats.put(UUID, cc);

                String ccp = (Main.chatColours.get(UUID) + Main.chatFormats.get(UUID)).replaceAll("null", "");
                ItemStack pre = new ItemStack(Material.EMPTY_MAP, 1);
                ItemUtil.addMetadata(pre, ccp + "Preview", true);

                previewInventory(e.getInventory(), pre, e.getSlot());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack i = e.getItem();

        if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
            if(Main.custoItemStack == null)
                return;

            if(i == null)
                return;

            if(i.equals(Main.custoItemStack))
                p.openInventory(Main.cmi());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String UUID = p.getUniqueId().toString();

		/*
		if(Main.nickColours.containsKey(p.getName()) || Main.nickFormats.containsKey(p.getName())){
			String cc = (Main.nickColours.get(p.getName()) + Main.nickFormats.get(p.getName())).replaceAll("null", "");
			e.setFormat(e.getFormat().replaceAll(p.getName(), cc + p.getName() + ChatColor.RESET));
		}*/

        if(Main.chatColours.containsKey(UUID) || Main.chatFormats.containsKey(UUID)){
            String cc = (Main.chatColours.get(UUID) + Main.chatFormats.get(UUID)).replaceAll("null", "");
            e.setMessage(cc + e.getMessage());
        }
    }

    //@EventHandler
    public void onJoin(PlayerJoinEvent e){
        //Player p = e.getPlayer();

        //if(!p.is)
    }

    public void sendTimeMessage(Player p, int time, String cmd){
        time /= 20;

        int secs = time % 60, mins = time / 60;

        p.sendMessage(Constants.WARN + "You need to wait " + mins + " minute(s) " + secs + " second(s) more to use " + cmd + " again");
    }

    public boolean containsAny(String s, String... other){
        return Arrays.stream(other)
                .map(c -> s.contains(c))
                .reduce(true, (a, b) -> a && b);
    }

    public static void previewInventory(Inventory i, ItemStack pre, int clickedSlot){
        ItemStack ckd = i.getItem(clickedSlot);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            i.setItem(clickedSlot, ckd);
        }, 20);

        i.setItem(clickedSlot, pre);
    }

    public static void sendCommand(String cmd){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public static void updateUsername(Player p){
        String cc = "";
        //String px = "";
        boolean in = false;
        String name = p.getName();
        User user = Main.essentials.getUser(p);
        //String UUID = p.getUniqueId().toString();

		/*if(Main.cPrefixes.containsKey(UUID)){
			px = Main.cPrefixes.get(UUID);
		}*/
        if(Main.nickColours.containsKey(name)){
            in = true;
            cc += Main.nickColours.get(name);
        }
        if(Main.nickFormats.containsKey(name)){
            in = true;
            cc += Main.nickFormats.get(name);
        }

        if(in){
            //p.sendMessage(user.getNickname() + "");

			/*if(user.getNickname() == null)
				user.setNickname(cc + user.getName());
			else
				user.setNickname(cc + user.getNickname());

			user.setDisplayNick();*/
            //p.sendMessage(user.getNickname() + " | " + (user.getNickname() == null));

            if(user.getNickname() == null || ChatColor.stripColor(user.getNickname()).equals(p.getName()))
                sendCommand(String.format("nick %1s %2s", p.getName(), cc + p.getName()));
            else
                sendCommand(String.format("nick %1s %2s", p.getName(), cc + ChatColor.stripColor(user.getNickname())));
        }
        else
            sendCommand(String.format("nick %1s %2s", p.getName(), ChatColor.stripColor(user.getNickname())));

        //p.sendMessage(user.getNickname());
        //p.sendMessage(String.format("pex user %1s prefix \"%2s\"", p.getName(), px));
    }

    public static String getBaseName(Player p){
        User user = Main.essentials.getUser(p);

        String nick = user.getNickname();

        if(user.getNickname() == null || ChatColor.stripColor(user.getNickname()).equals(p.getName()))
            return p.getName();
        return nick;
    }

    public static void setTablist(Player p, String cc){
        //Tablis
    }
}
