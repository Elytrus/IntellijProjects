package me.tlwv2.skmcplugin.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.skmcplugin.EListener;
import me.tlwv2.skmcplugin.Main;

public class CustomizationCommand extends PlayerOnlyCommand {
    static final String SET_PERM = "addon.use.customization.set";
    static final String GET_PERM = "addon.use.customization.get";

    public CustomizationCommand(){
        ILWrapper.addAllPerm(new String[]{
                SET_PERM, "Allows use of /customization set",
                GET_PERM, "Allows use of /customization get"
        }, EListener.plugin);
        ILWrapper.addCmd("customization", "Opens customization menu", EListener.plugin);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] args) {
        if(args.length > 0 && args[0].equalsIgnoreCase("set")){
            if(p.hasPermission(SET_PERM)){
                ItemStack h = p.getInventory().getItemInMainHand();

                if(h == null || h.getType() == Material.AIR){
                    Main.custoItemStack = null;
                    p.sendMessage(Constants.GOOD + "Removed customization menu open item!");
                }

                Main.custoItemStack = h;
                p.sendMessage(Constants.GOOD + "Set customization menu open item to current held item!");
            }
            else
                p.sendMessage(Constants.NOPERM);
        }
        else if(args.length > 0 && args[0].equalsIgnoreCase("get")){
            if(p.hasPermission(GET_PERM)){
                if(null == Main.custoItemStack){
                    p.sendMessage(Constants.WARN + "There is no Customization Inventory item!");
                    p.sendMessage(Constants.NOTE + "Use /customization to open the customization inventory!");
                }
                else{
                    p.getInventory().addItem(Main.custoItemStack);
                    p.sendMessage(Constants.GOOD + "Given customization menu opening item!");
                }
            }
            else
                p.sendMessage(Constants.NOPERM);
        }
        else
            p.openInventory(Main.cmi());

        return true;
    }

}
