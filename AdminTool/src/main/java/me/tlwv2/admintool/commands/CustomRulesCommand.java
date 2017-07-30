package me.tlwv2.admintool.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.admintool.customrules.RuleTable;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.misc.PlayerOnlyCommand;

public class CustomRulesCommand extends PlayerOnlyCommand {
    private static final String USAGE = Constants.USAGE + "/customrules <set:list> [rule name] [value]";

    final String VIEW = "addon.use.customrules.view";
    final String MODIFY = "addon.use.customrules.modify";

    public CustomRulesCommand() {
        ILWrapper.addAllPerm(new String[]{
                VIEW, "Allows you to view custom game rules",
                MODIFY, "Allows you to modify custom game rules"
        }, AdminTool.self);
        ILWrapper.addCmd("customrules", "Allows changing of custom game rules", AdminTool.self);
    }

    @Override
    public boolean onCommand_(Player p, Command arg1, String arg2, String[] arg3) {
        if(arg3.length == 0){
            p.sendMessage(USAGE);
            return true;
        }

        if(arg3[0].equals("set")){
            if(arg3.length != 3){
                p.sendMessage(USAGE);
                return true;
            }

            String rulename = arg3[1];
            RuleTable table = AdminTool.self.table();

            if(!table.exists(rulename)){
                p.sendMessage(Constants.WARN + "Rule does not exist!");
                return true;
            }

            String value = arg3[2];

            if(!table.isCorrectType(rulename, value)){
                p.sendMessage(Constants.WARN + "Incorrect value type! Value should be of type " + table.get(rulename).getType().simpleName());
                return true;
            }

            AdminTool.self.table().set(rulename, value);
            p.sendMessage(Constants.GOOD + "Rule " + rulename + " set to " + value + "!");
        }
        else if(arg3[0].equals("list")){
            p.sendMessage("§e-[Custom Gamerules]-");
            AdminTool.self.table().getAll()
                    .entrySet()
                    .stream()
                    .map(e -> String.format("§e- %s: %s | Type: %s", e.getKey(),
                            e.getValue().toString(),
                            e.getValue().getType().simpleName()))
                    .forEach(p::sendMessage);
        }

        return true;
    }

}
