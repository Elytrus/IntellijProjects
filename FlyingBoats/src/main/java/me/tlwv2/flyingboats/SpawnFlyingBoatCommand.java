package me.tlwv2.flyingboats;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Moses on 2017-08-12.
 */
public class SpawnFlyingBoatCommand extends PlayerOnlyCommand{

    public static final String PERM = "addon.use.spawnflyingboat";

    public SpawnFlyingBoatCommand() {
        ILWrapper.addCmd("spawnflyingboat", "SLIMER DID IT", FlyingBoats.self);
        ILWrapper.addPerm(PERM, "Gives permission to spawn flying boats", FlyingBoats.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        FlyingBoats.self.spawnFlyingBoat(p);

        return true;
    }
}
