package me.tlwv2.core.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tlwv2.core.Constants;

public abstract class PlayerOnlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(!(arg0 instanceof Player)){
            arg0.sendMessage(Constants.NOTPLAYER);
            return true;
        }

        Player p = (Player)arg0;

        return onCommand_(p, arg1, arg2, arg3);
    }

    public abstract boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3);
}
