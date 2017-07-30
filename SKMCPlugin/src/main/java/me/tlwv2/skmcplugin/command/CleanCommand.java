package me.tlwv2.skmcplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.skmcplugin.EListener;
import me.tlwv2.skmcplugin.Main;

public class CleanCommand implements CommandExecutor {

    private static final String PERM = "addon.use.clean";

    public CleanCommand(){
        ILWrapper.addPerm(PERM, "Allows use of /clean", EListener.plugin);
        ILWrapper.addCmd("clean", "Cleans config data", EListener.plugin);
    }

    @Override
    public boolean onCommand(CommandSender p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        if(Main.save){
            p.sendMessage(Constants.WARN + "Saving must be disabled!");

            return true;
        }

        EListener.plugin.getConfig().set("afktimer", null);
        EListener.plugin.getConfig().set("kittycannontimer", null);
        EListener.plugin.getConfig().set("lightningtimer", null);
        EListener.plugin.getConfig().set("chatcolours", null);
        EListener.plugin.getConfig().set("nickcolours", null);
        EListener.plugin.getConfig().set("chatformats", null);
        EListener.plugin.getConfig().set("nickformats", null);
        //EListener.plugin.getConfig().set("cprefixes", null);

        EListener.plugin.saveConfig();

        p.sendMessage(Constants.GOOD + "Data cleaned!");

        return true;
    }

}
