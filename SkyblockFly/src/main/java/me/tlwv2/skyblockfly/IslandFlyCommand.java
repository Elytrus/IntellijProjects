package me.tlwv2.skyblockfly;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.schematics.IslandBlock;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Moses on 2018-02-07.
 */
public class IslandFlyCommand extends PlayerOnlyCommand{
    public static final String PERM = "addon.use.islandfly";

    public IslandFlyCommand() {
        ILWrapper.addCmd("islandfly", "Toggles flying within your skyblock island", SkyblockFly.plugin);
        ILWrapper.addPerm(PERM, "Allows use of /islandfly", SkyblockFly.plugin);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        if(arg3.length > 0){
            if(arg3.length > 1){
                return false;
            }

            if(arg3[0].equals("on")){
                attemptFly(p);
            }
            else if(arg3[0].equals("off")){
                attemptStopFly(p);
            }
            else{
                return false;
            }
        }
        else{
            if(p.isFlying()){
                attemptStopFly(p);
            }
            else{
                attemptFly(p);
            }
        }

        return true;
    }

    private void attemptFly(Player p) {
        if(ASkyBlockAPI.getInstance().isOnIsland(p, p)){
            p.setAllowFlight(true);
            p.setFlying(true);
            p.sendMessage(Constants.GOOD + "You are now flying!");
        }
        else{
            p.sendMessage(Constants.WARN + "You are not on your island!");
        }
    }

    private void attemptStopFly(Player p) {
        p.setAllowFlight(false);
        p.setFlying(false);
        p.sendMessage(Constants.GOOD + "You are no longer flying!");
    }
}
