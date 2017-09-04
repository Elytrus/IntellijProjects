package me.tlwv2.admintool.commands;

import me.tlwv2.admintool.AdminTool;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * Created by Moses on 2017-08-21.
 */
public class GetPermsCommand implements CommandExecutor {
    public static final String PERM = "addon.use.getperms";

    public GetPermsCommand() {
        ILWrapper.addCmd("getperms", "Gets perms for specified group or player", AdminTool.self);
        ILWrapper.addPerm(PERM, "Allows use of /getperms", AdminTool.self);
    }

    @Override

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(PERM)){
            commandSender.sendMessage(Constants.NOPERM);
            return true;
        }

        if(strings.length != 3){
            return false;
        }

        if(strings[0].equalsIgnoreCase("player")){
            Player p = Bukkit.getPlayer(strings[1]);

            if(p == null) {
                commandSender.sendMessage(Constants.WARN + "Player not found!");
                return true;
            }

            String permissionToCheck = strings[2];
            final MutableBoolean match = new MutableBoolean();
            match.value = false;

            commandSender.sendMessage(String.format("\u00a7a-[Permissions for player %s]-", strings[1]));
            p.getEffectivePermissions().stream()
                    .map(PermissionAttachmentInfo::getPermission)
                    .forEach(permission -> {
                        if(permsMatch(permissionToCheck, permission)) {
                            commandSender.sendMessage(String.format("\u00a7a- %s", permission));
                            match.value = true;
                        }
                    });

            if(!match.value)
                commandSender.sendMessage("\u00a7Nothing was found :(");
        }
        else if(strings[0].equalsIgnoreCase("group")){

        }
        else{
            return false;
        }

        return true;
    }

    boolean permsMatch(String a, String b){
        String[] aTree = a.split("\\."), bTree = b.split("\\.");
        int lenToCheck = Math.max(aTree.length, bTree.length);

        for(int i = 0; i < lenToCheck; i++){
            String aPart = aTree[i], bPart = bTree[i];

            if(aPart.equals(bPart))
                continue;
            else if(aPart.equals("*") || bPart.equals("*"))
                return true;
            else
                return false;
        }

        return true;
    }
}

class MutableBoolean{
    boolean value;
}
