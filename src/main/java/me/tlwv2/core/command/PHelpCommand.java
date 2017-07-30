package me.tlwv2.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tlwv2.core.Constants;
import me.tlwv2.core.Core;
import me.tlwv2.core.infolist.ILWrapper;

public class PHelpCommand implements CommandExecutor {
    public PHelpCommand() {
        ILWrapper.addCmd("phelp", "Universal Maxwell Help Command", Core.self);
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(arg3.length < 1)
            ILWrapper.dumpAll(arg0);
        else{
            if(!ILWrapper.isRegisteredPlugin(arg3[0]))
                arg0.sendMessage(Constants.WARN + "Invalid Plugin!");
            else
                ILWrapper.dump(arg0, arg3[0]);
        }

        return true;
    }

}
