package me.tlwv2.skmcplugin.command;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.skmcplugin.EListener;
import me.tlwv2.skmcplugin.Main;
import me.tlwv2.skmcplugin.Prefix;

public class PrefixCommand extends PlayerOnlyCommand {
    private static final String PERM_ADD = "addon.use.prefix.add";
    private static final String PERM_EDIT = "addon.use.prefix.edit";
    private static final String PERM_REMOVE = "addon.use.prefix.remove";

    public PrefixCommand() {
        ILWrapper.addAllPerm(new String[]{
                PERM_ADD, "Allows you to add prefixes",
                PERM_EDIT, "Allows you to edit prefixes",
                PERM_REMOVE, "Allows you to remove prefixes"
        }, EListener.plugin);
        ILWrapper.addCmd("prefix", "Main prefix command", EListener.plugin);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] args) {
        if(args.length < 1){
            p.sendMessage(Constants.USAGE + "/prefix <add:edit:remove:list:help>");
            return true;
        }

        if(args[0].equals("add")){
            if(!p.hasPermission(PERM_ADD)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length < 3){
                p.sendMessage(Constants.USAGE + "/prefix add [name] [prefix]");
                return true;
            }

            if(Main.getByName(args[1]) != null){
                p.sendMessage(Constants.WARN + "Prefix already exists!");
                return true;
            }

            ItemStack is = p.getInventory().getItemInMainHand().clone();

            if(is == null || is.getType() == Material.AIR)
                is = new ItemStack(Material.STONE);

            String pfxStr = "";

            if(args.length != 3){
                pfxStr = Arrays.stream(Arrays.copyOfRange(args, 2, args.length)).collect(Collectors.joining(" "));
                pfxStr = pfxStr.substring(pfxStr.indexOf("\"") + 1, pfxStr.lastIndexOf("\""))
                        .replaceAll("&", "§");
            }
            else
                pfxStr = args[2];

            Prefix prefix = new Prefix(args[1], pfxStr, is);
            Main.prefixes.add(prefix);

            p.sendMessage(Constants.GOOD + "Added prefix " + prefix.getName());
        }
        else if(args[0].equals("edit")){
            if(!p.hasPermission(PERM_EDIT)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length < 3){
                p.sendMessage(Constants.USAGE + "/prefix edit [name] [prefix]");
                return true;
            }

            if(Main.getByName(args[1]) == null){
                p.sendMessage(Constants.WARN + "Prefix does not exist!");
                return true;
            }

            ItemStack is = p.getInventory().getItemInMainHand().clone();

            if(is == null || is.getType() == Material.AIR)
                is = new ItemStack(Material.STONE);

            p.sendMessage("" + is);

            String pfxStr = "";

            if(args.length != 3){
                pfxStr = Arrays.stream(Arrays.copyOfRange(args, 2, args.length)).collect(Collectors.joining(" "));
                pfxStr = pfxStr.substring(pfxStr.indexOf("\"") + 1, pfxStr.lastIndexOf("\""))
                        .replaceAll("&", "§");
            }
            else
                pfxStr = ChatColor.translateAlternateColorCodes('&', args[2]);

            Prefix prefix = Main.getByName(args[1]);
            prefix.setPrefix(pfxStr);
            prefix.setRep(is);

            p.sendMessage(Constants.GOOD + "Edited prefix " + prefix.getName());
        }
        else if(args[0].equals("remove")){
            if(!p.hasPermission(PERM_REMOVE)){
                p.sendMessage(Constants.NOPERM);
                return true;
            }

            if(args.length != 2){
                p.sendMessage(Constants.USAGE + "/prefix remove [name]");
                return true;
            }

            if(Main.getByName(args[1]) == null){
                p.sendMessage(Constants.WARN + "Prefix doesn't exist!");
                return true;
            }

            Prefix prefix = Main.getByName(args[1]);
            Main.prefixes.remove(prefix);

            p.sendMessage(Constants.GOOD + "Removed prefix " + prefix.getName());
        }
        else if(args[0].equals("list")){
            if(!Main.prefixes.isEmpty()){
                p.sendMessage(ChatColor.YELLOW + "--[List of Prefixes]--");
                Main.prefixes.stream()
                        .map(e -> String.format("- %1s : %2s§e | Permission: %3s", e.getName(), e.getPrefix(), e.getPermission()))
                        .map(e -> ChatColor.YELLOW + e)
                        .forEach(e -> p.sendMessage(e));
            }
            else
                p.sendMessage(Constants.WARN + "There are no Main.prefixes!");
        }
        return false;
    }

}
