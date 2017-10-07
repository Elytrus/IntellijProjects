package me.tlwv2.bosses.commands;

import me.tlwv2.bosses.Boss;
import me.tlwv2.bosses.Bosses;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses on 2017-10-06.
 */
public class KillBossesCommand implements CommandExecutor {

    public static final String PERM = "addon.use.killbosses";

    public KillBossesCommand() {
        ILWrapper.addCmd("killbosses", "Kills all bosses", Bosses.self);
        ILWrapper.addPerm(PERM, "Allows you to use /killbosses", Bosses.self);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        List<Boss> toBeRemoved = new ArrayList<>();

        Bosses.self.getBossList().forEach(boss -> {
            if(boss.isDead()) {
                toBeRemoved.add(boss);
                commandSender.sendMessage(Constants.WARN + "Boss " + boss + " is already dead.  Is this an error?");
            }
            else {
                Creature entity = boss.getEntity();
                entity.damage(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + 1);
            }
        });

        commandSender.sendMessage(Constants.GOOD + "Killed all bosses!");
    }
}
