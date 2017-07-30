package me.tlwv2.admintool.commands;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StackSizeCommand extends PlayerOnlyCommand{

    public static final String PERM = "addon.use.setstacksize";
    public static final String USAGE = ChatColor.DARK_RED + "Usage: /setstacksize <amount>";

    public StackSizeCommand() {
        ILWrapper.addCmd("setstacksize", "Sets stack size of held item", AdminTool.self);
        ILWrapper.addPerm(PERM, "Allows use of /setstacksize", AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(p.hasPermission(PERM)){
            ItemStack is = p.getInventory().getItemInMainHand();
            if(arg3.length != 1) {
                p.sendMessage(USAGE);
                return true;
            }
            try {
                int amount = Integer.parseInt(arg3[0]);
                if(amount > 127){
                    p.sendMessage(Constants.WARN + "Stack size must be a byte!");
                    return true;
                }
                is.setAmount(amount);
            } catch (NumberFormatException e) {
                p.sendMessage(USAGE);
            }
        }
        else
            p.sendMessage(Constants.NOPERM);

        return true;
    }
}
