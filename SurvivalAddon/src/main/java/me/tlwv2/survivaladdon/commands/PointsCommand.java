package me.tlwv2.survivaladdon.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.survivaladdon.Addons;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Moses on 2017-12-24.
 */
public class PointsCommand implements CommandExecutor {
    public static final String PERM = "addon.use.points";

    public PointsCommand() {
        ILWrapper.addCmd("points", "Shows point information for a player, or lists all point info if no player is specified",
                Addons.getInstance());
        ILWrapper.addPerm(PERM, "Allows use of /points", Addons.getInstance());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length == 0){
            commandSender.sendMessage("\u00a7e--[Point Info]--");
            for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
                commandSender.sendMessage("\u00a7e" + Addons.getInstance().manager().dumpPlayer(op));
            }
        }
        else if (strings.length == 1){
            if(!Addons.getInstance().manager().points(strings[0])){
                commandSender.sendMessage(Constants.WARN + "Point entry was not found!");
                return true;
            }
            if(!Addons.getInstance().manager().level(strings[0])){
                commandSender.sendMessage(Constants.WARN + "Level entry was not found!");
                return true;
            }
            if(!Addons.getInstance().manager().multiplier(strings[0])){
                commandSender.sendMessage(Constants.WARN + "Multiplier entry was not found!");
                return true;
            }

            commandSender.sendMessage("\u00a7e" + Addons.getInstance().manager().dumpPlayer(strings[0], strings[0]));
        }
        else{
            return false;
        }

        return true;
    }
}
