package me.tlwv2.skyblocktiers.commands;

import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.MessageQueue;
import me.tlwv2.skyblocktiers.SkyblockTiers;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Created by Moses on 2017-08-31.
 */
public class TestProbabilitiesCommand implements CommandExecutor {

    public static final String PERM = "addon.use.testprobabilities";

    public TestProbabilitiesCommand() {
        ILWrapper.addCmd("testprobabilities", "Tests the probabilities to roll certain ores depending on the tier"
        + ".  Generally used for debugging purposes", SkyblockTiers.self);
        ILWrapper.addPerm(PERM, "Allows use of /testprobabilities", SkyblockTiers.self);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        try {
            int tierToTest = Integer.parseInt(strings[1]);
            int numberOfTests = Integer.parseInt(strings[0]);

            if(tierToTest < 1 || tierToTest > 5){
                commandSender.sendMessage(Constants.WARN + "Invalid Tier!");
                return true;
            }

            int[] counts = new int[7]; //Coal, Iron, Redstone, Lapis, Gold, Diamond, Emerald

            for(int i = 0; i < numberOfTests; i++){
                int val = number(SkyblockTiers.self.rollForBlock(tierToTest));
                if(val == -1){
                    continue;
                }
                counts[val]++;
            }

            double total = Arrays.stream(counts).sum();
            int none = numberOfTests - (int)total;

            MessageQueue mq = new MessageQueue(commandSender, "\u00a7a");
            mq.append("-[Testing Results]-");
            mq.append("Amount of tests performed: " + numberOfTests);
            mq.append("Testing Tier: " + tierToTest);
            mq.append("- None: " + none + " | " + (none / total * 100) + "% chance");
            mq.append("- Coal: " + counts[0] + " | " + (counts[0] / total * 100) + "% chance");
            mq.append("- Iron: " + counts[1] + " | " + (counts[1] / total * 100) + "% chance");
            mq.append("- Redstone: " + counts[2] + " | " + (counts[2] / total * 100) + "% chance");
            mq.append("- Lapis: " + counts[3] + " | " + (counts[3] / total * 100) + "% chance");
            mq.append("- Gold: " + counts[4] + " | " + (counts[4] / total * 100) + "% chance");
            mq.append("- Diamond: " + counts[5] + " | " + (counts[5] / total * 100) + "% chance");
            mq.append("- Emerald: " + counts[6] + " | " + (counts[6] / total * 100) + "% chance");
            mq.executeAll();
            mq.flush();
        }
        catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
            return false;
        }

        return true;
    }

    int number(Material m){
        if(m == null)
            return -1;

        switch(m){
            case COAL_ORE:
                return 0;
            case IRON_ORE:
                return 1;
            case REDSTONE_ORE:
                return 2;
            case LAPIS_ORE:
                return 3;
            case GOLD_ORE:
                return 4;
            case DIAMOND_ORE:
                return 5;
            case EMERALD_ORE:
                return 6;
            default:
                return -2;
        }
    }
}
