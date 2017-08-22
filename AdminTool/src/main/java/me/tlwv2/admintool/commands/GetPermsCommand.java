package me.tlwv2.admintool.commands;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Moses on 2017-08-21.
 */
public class GetPermsCommand implements CommandExecutor {
    public static final String PERM = "addon.use.getperms";

    public GetPermsCommand() {
        ILWrapper.addCmd("getperms", "Gets perms for specified group or player", AdminTool.self);
        ILWrapper.addPerm(PERM, "Allows use of /getperms", AdminTool.self);
    }

    @Override

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length != 3){
            return false;
        }

        if(strings[0].equalsIgnoreCase("player")){
            OfflinePlayer p = Bukkit.getPlayer(strings[1]);

            if(p == null) {
                commandSender.sendMessage(Constants.WARN + "Attempting OfflinePlayer check");

                for (OfflinePlayer pp : Bukkit.getOfflinePlayers()) {
                    if (pp.getName().equals(strings[1]))
                        p = pp;
                }

                if (p == null) {
                    commandSender.sendMessage(Constants.WARN + "Player not found!");
                    return true;
                }
            }

            String perm = strings[2];
        }
        else if(strings[0].equalsIgnoreCase("group")){

        }
        else{
            return false;
        }

        return true;
    }
}
