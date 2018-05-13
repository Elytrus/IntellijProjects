package me.tlwv2.kitsp.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.core.utils.ItemUtil;
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
    static final String USAGE_MESSAGE = ChatColor.RED + "Usage: /kitsplus <add:remove:edit:list:gui:needspermissions:set:folder>";

    private static final String USAGE_MESSAGE_ADD = ChatColor.RED + "Usage: /kitsplus add <name> [display name]";
    private static final String USAGE_MESSAGE_REMOVE = ChatColor.RED + "Usage: /kitsplus remove <name>";
    private static final String USAGE_MESSAGE_EDIT = ChatColor.RED + "Usage: /kitsplus edit <name> <icon:inventory:effects:name:all> [display name (all or name only)]";
    public static final String MODIFY_NO_PERMISSIONS = "addon.use.kitsplus.modifyneedspermissions";
    public static final String ADD_PERM = "addon.use.kitsplus.add";
    public static final String REMOVE_PERM = "addon.use.kitsplus.remove";
    public static final String EDIT_PERM = "addon.use.kitsplus.edit";
    public static final String FOLDER_PERM = "addons.use.kitsplus.folder";

    private static Main main = null;

    public KitsPlusCommand() {
        ILWrapper.addCmd("kitsplus", "Main KitsPlus command, can add, remove, edit, and list kits", Main.instance());
        ILWrapper.addAllPerm(new String[]{
                MODIFY_NO_PERMISSIONS, "Allows the ability to toggle whether Kits require permissions or not",
                ADD_PERM, "Allows the ability to add kits",
                REMOVE_PERM, "Allows the ability to remove kits",
                EDIT_PERM, "Allows the ability to edit kits",
                Main.PLACE_SIGN_PERM, "Allows the ability to place down KitsPlus signs"
        }, Main.instance());
        main = Main.instance();
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] args) {
        if(args.length == 0){
            p.sendMessage(USAGE_MESSAGE);
            return true;
        }

        if(args[0].equalsIgnoreCase("needspermissions")){
            if(!p.hasPermission(MODIFY_NO_PERMISSIONS)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(Main.needsPerms){
                Main.needsPerms = false;
                p.sendMessage(Main.KITS_PLUS_LOGO + "Permissions now not required!");
            }
            else{
                Main.needsPerms = true;
                p.sendMessage(Main.KITS_PLUS_LOGO + "Permissions now required!");
            }
        }

        if(args[0].equalsIgnoreCase("add")){
            if(!p.hasPermission(ADD_PERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length > 2){
                String displayName = Arrays.stream(Arrays.copyOfRange(args, 2, args.length))
                        .collect(Collectors.joining(" "));

                p.sendMessage(Main.KITS_PLUS_LOGO + "Successfully added Kit " + args[1]);
                Main.instance().getKits().put(args[1], new Kit(p, args[1], displayName));
            }
            else{
                p.sendMessage(USAGE_MESSAGE_ADD);
            }
        }
        else if(args[0].equalsIgnoreCase("remove")){
            if(!p.hasPermission(REMOVE_PERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length == 2){
                if(main.getKits().containsKey(args[1])){
                    p.sendMessage(Main.KITS_PLUS_LOGO + "Successfully removed Kit " + args[1]);
                    Main.instance().getKits().remove(args[1]);
                }
                else{
                    p.sendMessage(Main.KITS_PLUS_LOGO + ChatColor.RED + "Kit does not exist!");
                }
            }
            else{
                p.sendMessage(USAGE_MESSAGE_REMOVE);
            }
        }
        else if(args[0].equalsIgnoreCase("edit")){
            if(!p.hasPermission(EDIT_PERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length >= 3 && Arrays.asList("inventory", "icon", "effects", "all", "name").contains(args[2])){
                if(Main.instance().getKits().containsKey(args[1])){
                    if(args[2].equals("inventory")){
                        Main.instance().getKits().get(args[1]).setContents(p);
                        p.sendMessage(Main.KITS_PLUS_LOGO + "Changed contents of Kit " + args[1]);
                        p.setHealth(0.0);
                    }
                    else if(args[2].equals("icon")){
                        Main.instance().getKits().get(args[1]).setIcon(p);
                        p.sendMessage(Main.KITS_PLUS_LOGO + "Changed icon of Kit " + args[1]);
                    }
                    else if(args[2].equals("effects")){
                        Main.instance().getKits().get(args[1]).setEffects(p);
                        p.sendMessage(Main.KITS_PLUS_LOGO + "Changed potion effects of Kit " + args[1]);
                    }
                    else if(args[2].equals("name")){
                        if(args.length < 4){
                            p.sendMessage(USAGE_MESSAGE_EDIT);
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
                        p.sendMessage(Main.KITS_PLUS_LOGO + "Changed everything about Kit " + args[1]);
                    }
                }
                else{
                    p.sendMessage(Main.KITS_PLUS_LOGO + ChatColor.RED + "Kit does not exist!");
                }
            }
            else{
                p.sendMessage(USAGE_MESSAGE_EDIT);
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
                p.sendMessage(Main.KITS_PLUS_LOGO + ChatColor.RED + "There are no kits!");
            }
        }
        else if(args[0].equalsIgnoreCase("gui")){
            if(!Main.instance().getKits().isEmpty()){
                Inventory inv = Bukkit.createInventory(null, 54, Main.KITS_PLUS_GUI_NAME);

                for(Kit k : Main.instance().getKits().values()){
                    inv.addItem(k.getIcon());
                }

                p.openInventory(inv);
            }
            else{
                p.sendMessage(Main.KITS_PLUS_LOGO + ChatColor.RED + "There are no kits!");
            }
        }
        else if(args[0].equalsIgnoreCase("folder")){
            if(args.length < 2){
                p.sendMessage(USAGE_MESSAGE);
                return true;
            }

            if(!p.hasPermission(FOLDER_PERM)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args[1].equalsIgnoreCase("new")){
                //
            }
            else if(args[1].equalsIgnoreCase("edit")){
                //
            }
            else if(args[1].equalsIgnoreCase("editmain")){
                //
            }
            else if(args[1].equalsIgnoreCase("list")){
                //
            }
            else if(args[1].equalsIgnoreCase("remove")){
                //
            }
            else{
                p.sendMessage("\u00a7c/kitsplus folder <new:edit:editmain:list:remove>");
            }
        }
        else if(args[0].equalsIgnoreCase("set")){
            if(args.length != 2){
                p.sendMessage("\u00a7c/kitsplus set <kit>");
                return true;
            }

            if(!p.hasPermission("kitsplus.use." + args[1])){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(!Main.instance().hasKit(args[1])){
                p.sendMessage(Main.KITS_PLUS_LOGO + "\u00a7cKit does not exist!");
                return true;
            }

            Main.instance().getKit(args[1]).apply(p);
        }

        return true;
    }
}
