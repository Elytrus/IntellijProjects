package me.tlwv2.skyblocktiers.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.skyblocktiers.SkyblockTiers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Moses on 2017-08-22.
 */
public class SkyblockMiningUpgradeCommand extends PlayerOnlyCommand {

    public static final String PERM = "addon.use.skyblockminingupgrade";

    public SkyblockMiningUpgradeCommand() {
        ILWrapper.addCmd("skyblockminingupgrade", "Opens up the Skyblock Tier Upgrade UI", SkyblockTiers.self);
        ILWrapper.addPerm(PERM, "Allows use of /skyblockminingupgrade", SkyblockTiers.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        Inventory ui = Bukkit.createInventory(null, 9, SkyblockTiers.UPGRADE_INVENTORY_NAME);
        int tier = SkyblockTiers.self.getTier(p);

        SkyblockTiers.self.buildUpgradeInventory(ui,
                SkyblockTiers.self.getTier(p),
                SkyblockTiers.self.getEconomy().getBalance(p));

        p.openInventory(ui);
        return true;
    }
}
