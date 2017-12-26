package me.tlwv2.survivaladdon.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.survivaladdon.Addons;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Moses on 2017-12-25.
 */
public class MultiplierSetCommand implements CommandExecutor {

    public static final String PERM = "addon.use.multiplierset";

    public MultiplierSetCommand() {
        ILWrapper.addCmd("multiplierset", "Sets the point multiplier for a UUID", Addons.getInstance());
        ILWrapper.addPerm(PERM, "Allows use of /multiplierset", Addons.getInstance());
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
            float multiplier = Float.parseFloat(strings[1]);
            Addons.getInstance().manager().setMultiplier(strings[0], multiplier);
            commandSender.sendMessage(Constants.GOOD + "Success!");
        }
        catch(NumberFormatException e){
            commandSender.sendMessage(Constants.WARN + "Point value must be a float!");
        }

        return true;
    }
}
