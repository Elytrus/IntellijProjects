package me.tlwv2.skmcplugin.command;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.skmcplugin.EListener;

public class BamCommand extends PlayerOnlyCommand {

    private static final String PERM = "addon.use.safelightning";

    public BamCommand() {
        ILWrapper.addPerm(PERM, "Allows you to use /bam", EListener.plugin);
        ILWrapper.addCmd("bam", "Launches safe lightning", EListener.plugin);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(p.hasPermission(PERM)){
            p.getWorld().strikeLightningEffect(p
                    .getTargetBlock((Set<Material>)null, Integer.MAX_VALUE).getLocation());
        }
        else
            p.sendMessage(Constants.NOPERM);

        return true;
    }

}
