package me.tlwv2.admintool.commands;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RenameCommand extends PlayerOnlyCommand {
    final String CLR = "addon.use.rename.clr";
    final String NML = "addon.use.rename";

    public RenameCommand(){
        ILWrapper.addAllPerm(new String[]{
                NML, "Allows you to use /rename",
                CLR, "Allows renaming in colour"
        }, AdminTool.self);
        ILWrapper.addCmd("rename", "Renames Items", AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] args) {
        if(args.length == 0){
            p.sendMessage(Constants.USAGE + "/rename <name>");
            return true;
        }

        if(!p.hasPermission(NML)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        String name = "";

        if(args.length != 1){
            name = Arrays.stream(Arrays.copyOfRange(args, 0, args.length)).collect(Collectors.joining(" "));
            name = ChatColor.translateAlternateColorCodes(
                    '&', name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\"")));
        }
        else
            name = ChatColor.translateAlternateColorCodes('&', args[0]);

        if(!p.hasPermission(CLR)){
            p.sendMessage(Constants.WARN + "You do not have permission to use colour codes with /rename, new name will not have any colour codes!");
            name = ChatColor.stripColor(name);
        }

        if(ChatColor.stripColor(name).equals(name))
            name = ChatColor.RESET + name;

        ItemUtil.addMetadata(p.getInventory().getItemInMainHand(), name, false);
        p.sendMessage(Constants.GOOD + "Renamed Item!");

        return true;
    }
}
