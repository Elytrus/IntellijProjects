package me.tlwv2.survivaladdon.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.survivaladdon.Addons;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Moses on 2017-12-24.
 */
public class PointSetCommand implements CommandExecutor {

    public static final String PERM = "addon.use.pointset";

    public PointSetCommand() {
        ILWrapper.addCmd("pointset", "Sets points for a plyer", Addons.getInstance());
        ILWrapper.addPerm(PERM, "Allows use of /pointset", Addons.getInstance());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length != 2){
            return false;
        }

        try{
            int points = Integer.parseInt(strings[1]);
            Addons.getInstance().manager().setPoints(strings[0], points);
            commandSender.sendMessage(Constants.GOOD + "Success!");
        }
        catch(NumberFormatException e){
            commandSender.sendMessage(Constants.WARN + "Point value must be an integer!");
        }

        return true;
    }
}
