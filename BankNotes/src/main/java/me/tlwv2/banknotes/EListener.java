package me.tlwv2.banknotes;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2017-09-19.
 */
public class EListener implements Listener{

    public static final String CLAIM_PERM = "addon.banknotes.claim";

    public EListener(BankNotes plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        ILWrapper.addPerm(CLAIM_PERM, "Allows you to claim Bank Notes", BankNotes.self);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Action action = e.getAction();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            Player p = e.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();

            if(BankNotes.self.isBankNote(item)){
                if(!p.hasPermission(CLAIM_PERM)){
                    p.sendMessage(Constants.NOPERM);
                    return;
                }

                double amount = BankNotes.self.getBalanceOfBankNote(item);
                BankNotes.economy.depositPlayer(p, amount);

                if(item.getAmount() > 1)
                    item.setAmount(item.getAmount() - 1);
                else
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                p.sendMessage(Constants.GOOD + "Successfully cashed in Bank Note!");
                p.sendMessage(Constants.NOTE + "Your new balance is $" + BankNotes.economy.getBalance(p));

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent e){
        for(ItemStack i : e.getInventory().getMatrix()){
            if(BankNotes.self.isBankNote(i)){
                e.getInventory().setResult(null);
                return;
            }
        }
    }
}
