package me.tlwv2.kitsp.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.core.utils.ItemUtil;
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

    private static final String USAGEMESSAGEADD = ChatColor.RED + "Usage: /kitsplus add <name> [display name]";
    private static final String USAGEMESSAGEREM = ChatColor.RED + "Usage: /kitsplus remove <name>";
    private static final String USAGEMESSAGEEDI = ChatColor.RED + "Usage: /kitsplus edit <name> <icon:inventory:effects:name:all> [display name (all or name only)]";
    public static final String MODIFYNPPERM = "addon.use.kitsplus.modifyneedspermissions";
    public static final String ADDPERM = "addon.use.kitsplus.add";
    public static final String REMOVEPERM = "addon.use.kitsplus.remove";
    public static final String EDITPERM = "addon.use.kitsplus.edit";

    public KitsPlusCommand() {
        ILWrapper.addCmd("kitsplus", "Main KitsPlus command, can add, remove, edit, and list kits", Main.instance());
        ILWrapper.addAllPerm(new String[]{
                MODIFYNPPERM, "Allows the ability to toggle whether Kits require permissions or not",
                ADDPERM, "Allows the ability to add kits",
                REMOVEPERM, "Allows the ability to remove kits",
                EDITPERM, "Allows the ability to edit kits",
                EListener.PLACESIGNPERM, "Allows the ability to place down KitsPlus signs"
        }, Main.instance());
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

            if(Main.instance().getKits().values().size() > 54){
                p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "Max amount of kits already added! (54)");
                return true;
            }

            if(args.length > 2){
                String displayName = Arrays.stream(Arrays.copyOfRange(args, 2, args.length))
                        .collect(Collectors.joining(" "));

                p.sendMessage(Main.KITSPLOGO + "Successfully added Kit " + args[1]);
                Main.instance().getKits().put(args[1], new Kit(p, args[1], displayName));
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
                if(Main.instance().getKits().containsKey(args[1])){
                    p.sendMessage(Main.KITSPLOGO + "Successfully remove Kit " + args[1]);
                    Main.instance().getKits().remove(args[1]);
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

            if(args.length >= 3 && Arrays.asList("inventory", "icon", "effects", "all", "name").contains(args[2])){
                if(Main.instance().getKits().containsKey(args[1])){
                    if(args[2].equals("inventory")){
                        Main.instance().getKits().get(args[1]).setContents(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed contents of Kit " + args[1]);
                    }
                    else if(args[2].equals("icon")){
                        Main.instance().getKits().get(args[1]).setIcon(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed icon of Kit " + args[1]);
                    }
                    else if(args[2].equals("effects")){
                        Main.instance().getKits().get(args[1]).setEffects(p);
                        p.sendMessage(Main.KITSPLOGO + "Changed potion effects of Kit " + args[1]);
                    }
                    else if(args[2].equals("name")){
                        if(args.length < 4){
                            p.sendMessage(USAGEMESSAGEEDI);
                            return true;
                        }

                        String displayName = ChatColor.translateAlternateColorCodes('&'
                                ,Arrays.stream(Arrays.copyOfRange(args, 3, args.length))
                                .collect(Collectors.joining(" ")));

                        Kit k = Main.instance().getKits().get(args[1]);
                        k.setIcon(ItemUtil.addMetadata(k.getIcon(), displayName, false));
                        p.sendMessage(Constants.GOOD + "Changed name of kit " + args[1] + " to " + displayName + "\u00a7a!");
                    }
                    else if(args[2].equals("all")) {
                        String displayName = Arrays.stream(Arrays.copyOfRange(args, 3, args.length))
                                .collect(Collectors.joining(" "));

                        if (args.length < 4)
                            displayName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

                        Main.instance().getKits().get(args[1]).reset(p, args[1], displayName);
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
            if(!Main.instance().getKits().isEmpty()){
                p.sendMessage(ChatColor.YELLOW + "---[KitsPlus Kits]---");

                for(String name : Main.instance().getKits().keySet()){
                    p.sendMessage(" - " + name + " | kitsplus.use." + name);
                }
            }
            else{
                p.sendMessage(Main.KITSPLOGO + ChatColor.RED + "There are no kits!");
            }
        }
        else if(args[0].equalsIgnoreCase("gui")){
            if(!Main.instance().getKits().isEmpty()){
                Inventory inv = Bukkit.createInventory(null, 54, Main.KITSPGUINAME);

                for(Kit k : Main.instance().getKits().values()){
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
