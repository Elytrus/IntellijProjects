package me.tlwv2.skmcplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.skmcplugin.EListener;
import me.tlwv2.skmcplugin.Main;

public class ToggleSaveCommand implements CommandExecutor {

    private static final String PERM = "addon.use.togglesave";

    public ToggleSaveCommand() {
        ILWrapper.addPerm(PERM, "Allows you to use /togglesave", EListener.plugin);
        ILWrapper.addCmd("togglesave", "Toggles option to save non-critical information", EListener.plugin);
    }

    @Override
    public boolean onCommand(CommandSender p, Command arg1, String arg2, String[] args) {
        if(args.length == 1 && args[0].equals("get")){
            p.sendMessage(Constants.NOTE + "Saving is " + (Main.save ? "enabled" : "disabled"));

            return true;
        }

        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        Main.save = !Main.save;

        p.sendMessage(Constants.GOOD + "Saving is now " + (Main.save ? "on" : "off"));

        return true;
    }

}
