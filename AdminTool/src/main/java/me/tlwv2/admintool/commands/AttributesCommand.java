package me.tlwv2.admintool.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;

public class AttributesCommand extends PlayerOnlyCommand {
    static final String PERM = "addon.use.attributes";

    public AttributesCommand() {
        ILWrapper.addPerm(PERM, "Allows use of /attributes command", AdminTool.self);
        ILWrapper.addCmd("attributes", "Changes attributes of held item", AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {

        return true;
    }

}
