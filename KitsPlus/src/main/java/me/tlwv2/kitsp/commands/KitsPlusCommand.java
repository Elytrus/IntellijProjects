package me.tlwv2.kitsp.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.kitsp.EListener;
import me.tlwv2.kitsp.Kit;
import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class KitsPlusCommand extends PlayerOnlyCommand{
    static final String USAGEMESSAGE = ChatColor.RED + "Usage: /kitsplus <add:remove:edit:list:gui:needspermissions>";

    static final String USAGEMESSAGEADD = ChatColor.RED + "Usage: /kitsplus add <name> [display name]";
    static final String USAGEMESSAGEREM = ChatColor.RED + "Usage: /kitsplus remove <name>";
    static final String USAGEMESSAGEEDI = ChatColor.RED + "Usage: /kitsplus edit <name> <icon:inventory:effects:all> [display name (all only)]";
    public static final String MODIFYNPPERM = "addon.use.kitsplus.modifyneedspermissions";
    public static final String ADDPERM = "addon.use.kitsplus.add";
    public static final String REMOVEPERM = "addon.use.kitsplus.remove";
    public static final String EDITPERM = "addon.use.kitsplus.edit";

    public KitsPlusCommand() {
        ILWrapper.addCmd("kitsplus", "Main KitsPlus command, can add, remove, edit, and list kits", Main.self);
        ILWrapper.addAllPerm(new String[]{
                MODIFYNPPERM, "Allows the ability to toggle whether Kits require permissions or not",
                ADDPERM, "Allows the ability to add kits",
                REMOVEPERM, "Allows the ability to remove kits",
                EDITPERM, "Allows the ability to edit kits",
                EListener.PLACESIGNPERM, "Allows the ability to place down KitsPlus signs"
        }, Main.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] args) {
        if(args.length == 0){
            p.sendMessage(USAGEMESSAGE);
            return true;
        }

        if(args[0].equalsIgnoreCase("needspermissions")){
            if(!p.hasPermission(MODIFYNPPERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(Main.needsPerms){
                Main.needsPerms = false;
                p.sendMessage(Main.KITSPLOGO + "Permissions now not required!");
            }
            else{
                Main.needsPerms = true;
                p.sendMessage(Main.KITSPLOGO + "Permissions now required!");
            }
        }

        if(args[0].equalsIgnoreCase("add")){
            if(!p.hasPermission(ADDPERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(Main.kits.values().size() > 54){
                p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "Max amount of kits already added! (54)");
                return true;
            }

            if(args.length > 2){
                String displayName = Arrays.stream(Arrays.copyOfRange(args, 2, args.length))
                        .collect(Collectors.joining(" "));

                p.sendMessage(Main.KITSPLOGO + "Successfully added Kit " + args[1]);
                Main.kits.put(args[1], new Kit(p, args[1], displayName));
            }
            else{
                p.sendMessage(USAGEMESSAGEADD);
            }
        }
        else if(args[0].equalsIgnoreCase("remove")){
            if(!p.hasPermission(REMOVEPERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length == 2){
                if(Main.kits.containsKey(args[1])){
                    p.sendMessage(Main.KITSPLOGO + "Successfully remove Kit " + args[1]);
                    Main.kits.remove(args[1]);
                }
                else{
                    p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "Kit does not exist!");
                }
            }
            else{
                p.sendMessage(USAGEMESSAGEREM);
            }
        }
        else if(args[0].equalsIgnoreCase("edit")){
            if(!p.hasPermission(EDITPERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length >= 3 && Arrays.asList("inventory", "icon", "effects", "all").contains(args[2])){
                if(Main.kits.containsKey(args[1])){
                    if(args[2].equals("inventory")){
                        Main.kits.get(args[1]).setContents(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed contents of Kit " + args[1]);
                    }
                    else if(args[2].equals("icon")){
                        Main.kits.get(args[1]).setIcon(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed icon of Kit " + args[1]);
                    }
                    else if(args[2].equals("effects")){
                        Main.kits.get(args[1]).setEffects(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed potion effects of Kit " + args[1]);
                    }
                    else if(args[2].equals("all")){
                        String displayName = Arrays.stream(Arrays.copyOfRange(args, 3, args.length))
                                .collect(Collectors.joining(" "));

                        if(args.length < 4)
                            displayName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

                        Main.kits.get(args[1]).reset(p, args[1], displayName);
                        p.sendMessage(Main.KITSPLOGO + "Changed everything about Kit " + args[1]);
                    }
                }
                else{
                    p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "Kit does not exist!");
                }
            }
            else{
                p.sendMessage(USAGEMESSAGEEDI);
            }
        }
        else if(args[0].equalsIgnoreCase("list")){
            if(!Main.kits.isEmpty()){
                p.sendMessage(ChatColor.YELLOW + "---[KitsPlus Kits]---");

                for(String name : Main.kits.keySet()){
                    p.sendMessage(" - " + name + " | kitsplus.use." + name);
                }
            }
            else{
                p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "There are no kits!");
            }
        }
        else if(args[0].equalsIgnoreCase("gui")){
            if(!Main.kits.isEmpty()){
                Inventory inv = Bukkit.createInventory(null, 54, Main.KITSPGUINAME);

                for(Kit k : Main.kits.values()){
                    inv.addItem(k.getIcon());
                }

                p.openInventory(inv);
            }
            else{
                p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "There are no kits!");
            }
        }

        return true;
    }
}
