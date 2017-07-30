package me.tlwv2.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tlwv2.core.Constants;
import me.tlwv2.core.Core;
import me.tlwv2.core.infolist.ILWrapper;

public class ListDependsCommand implements CommandExecutor {
    static final String PERM = "pcore.use.listdependers";

    public ListDependsCommand() {
        ILWrapper.addPerm(PERM, "Permission to use /listdependers", Core.self);
        ILWrapper.addCmd("listdependers", "Lists plugins that depends on PCore to function", Core.self);
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(!Constants.HAS_PERM.apply(PERM, arg0))
            return true;

        arg0.sendMessage("§e-[Plugins Dependent on This Plugin]-");
        for(String s : ILWrapper.getRegisteredPlugins())
            arg0.sendMessage("§e- " + s);
        return true;
    }
}
