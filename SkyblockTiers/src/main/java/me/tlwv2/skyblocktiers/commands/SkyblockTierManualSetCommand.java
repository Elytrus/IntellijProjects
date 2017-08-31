package me.tlwv2.skyblocktiers.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.skyblocktiers.SkyblockTiers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

import static me.tlwv2.skyblocktiers.SkyblockTiers.self;

/**
 * Created by Moses on 2017-08-22.
 */
public class SkyblockTierManualSetCommand implements CommandExecutor {
    public static final String PERM = "addon.use.skyblocktiermanualset";
    public static final String USAGE = "/skyblocktiermanualset <set:list> [entry] [value]";

    public SkyblockTierManualSetCommand() {
        ILWrapper.addCmd("skyblocktiermanualset", "Allows manual setting of skyblock mining tiers" +
                ".  Note that islands with tier 0 will not be displayed in the list", self);
        ILWrapper.addPerm(PERM, "Allows user of /skyblocktiermanualset", self);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length < 1){
            commandSender.sendMessage(USAGE);
            return true;
        }

        if(strings[0].equalsIgnoreCase("list")){
            commandSender.sendMessage("§e-[Skyblock Mining Tierlist]-");
            for(Map.Entry<String, Integer> entry : self.getTierList().entrySet()){
                commandSender.sendMessage(String.format("§e- Owner %s | Mining Tier: %d", entry.getKey(), entry.getValue()));
            }
        }
        else if(strings[0].equalsIgnoreCase("set")){
            try{
                int tier = Integer.parseInt(strings[2]);
                SkyblockTiers.self.setTier(strings[1], tier);
            }
            catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                commandSender.sendMessage(USAGE);
                return true;
            }
        }
        else{
            commandSender.sendMessage(USAGE);
            return true;
        }

        return true;
    }
}
