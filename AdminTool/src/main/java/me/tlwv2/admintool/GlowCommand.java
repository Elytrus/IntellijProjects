package me.tlwv2.admintool;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Moses on 2018-03-23.
 */
public class GlowCommand extends PlayerOnlyCommand {

    public static final String PERM = "addon.use.glow";

    public GlowCommand() {
        ILWrapper.addCmd("glow", "Literally just makes your held item glow", AdminTool.self);
        ILWrapper.addPerm("glow", PERM, AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
        }

        p.getInventory().getItemInMainHand().addEnchantment(Constants.glow, 1);

        return true;
    }
}
