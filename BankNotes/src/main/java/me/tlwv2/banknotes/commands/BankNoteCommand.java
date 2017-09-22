package me.tlwv2.banknotes.commands;

import me.tlwv2.banknotes.BankNotes;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;
import me.tlwv2.core.utils.ItemData;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class BankNoteCommand extends PlayerOnlyCommand {

    public static final String PERM = "addon.use.banknote";

    public BankNoteCommand() {
        ILWrapper.addCmd("banknote", "Creates a banknote", BankNotes.self);
        ILWrapper.addPerm(PERM, "Allows use of /banknote", BankNotes.self);
    }

    @Override

    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(!p.hasPermission(PERM)){
            p.sendMessage(Constants.NOPERM);
            return true;
        }

        try{
            double amount = Double.parseDouble(arg3[0]);
            double currentBalance = BankNotes.economy.getBalance(p);

            if(currentBalance < amount){
                p.sendMessage(Constants.WARN + "Insufficient Funds!");
                p.sendMessage(Constants.WARN + "You need $" + (amount - currentBalance) +
                    " more to create the banknote");
                return true;
            }

            BankNotes.economy.withdrawPlayer(p, amount);

            p.getInventory().addItem(BankNotes.self.constructBankNote(amount));

            p.sendMessage(Constants.GOOD + "Success!");
            p.sendMessage(Constants.NOTE + "Your new balance is $" + BankNotes.economy.getBalance(p));
        }
        catch(Exception e){
            return false;
        }

        return true;
    }
}
