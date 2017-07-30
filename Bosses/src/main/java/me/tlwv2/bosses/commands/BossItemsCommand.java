package me.tlwv2.bosses.commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tlwv2.bosses.Bosses;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;

public class BossItemsCommand extends PlayerOnlyCommand {
    private static final String PERMISSION = "bosses.use.bossitems";

    public BossItemsCommand(){
        ILWrapper.addCmd("bossitems", "Allows you to access the boss spawn items", Bosses.self);
        ILWrapper.addPerm(PERMISSION, "Allows use of /bossitems", Bosses.self);
    }

    @Override
    public boolean onCommand_(Player arg0, Command arg1, String arg2, String[] arg3) {
        if(!arg0.hasPermission(PERMISSION)){
            arg0.sendMessage(Constants.NOPERM);
            return true;
        }

        Set<ItemStack> items = Bosses.self.spawnItems();
        Inventory i = Bukkit.createInventory(null, (items.size() / 9) * 9 + 9, "Boss Spawn Eggs");
        i.addItem(items.toArray(new ItemStack[0]));
        ((Player)arg0).openInventory(i);

        return true;
    }

}
