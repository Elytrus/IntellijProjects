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
 * Created by Moses on 2017-12-25.
 */
public class LevelUpdateCommand implements CommandExecutor {

    public static final String PERM = "addon.use.levelupdate";

    public LevelUpdateCommand() {
        ILWrapper.addCmd("levelupdate", "Updates levels for players", Addons.getInstance());
        ILWrapper.addPerm(PERM, "Allows use of /levelupdate", Addons.getInstance());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length == 0){
            for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
                Addons.getInstance().manager().incrementPoints(op.getUniqueId().toString(), 0);
            }

            commandSender.sendMessage(Constants.GOOD + "Success!");
        }
        else if(strings.length == 1){
            if(!Addons.getInstance().manager().points(strings[0])){
                commandSender.sendMessage(Constants.WARN + "Point entry does not exist?");
                return true;
            }

            Addons.getInstance().manager().incrementPoints(strings[0], 0);
            commandSender.sendMessage(Constants.GOOD + "Success!");
        }
        else{
            return false;
        }

        return true;
    }
}
