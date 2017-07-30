package me.tlwv2.core;

import java.util.function.BiFunction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import me.tlwv2.core.misc.Glow;

public class Constants {
    public static final String NOTE = ChatColor.BOLD + "" + ChatColor.YELLOW + "[" + ChatColor.RED + "!" + ChatColor.YELLOW + "] ";
    public static final String WARN = ChatColor.BOLD + "" + ChatColor.DARK_RED + "[!] ";
    public static final String GOOD = ChatColor.BOLD + "" + ChatColor.GREEN + "[!] ";
    public static final String NOPERM = WARN + "You do not have the permission to do that!";
    public static final String USAGE = WARN + "Usage: ";
    public static final String NOTPLAYER = WARN + "A Player must execute this command!";
    public static final BiFunction<String, CommandSender, Boolean> HAS_PERM = new BiFunction<String, CommandSender, Boolean>(){

        @Override
        public Boolean apply(String arg0, CommandSender arg1) {
            if(arg1.hasPermission(arg0))
                return true;
            arg1.sendMessage(NOPERM);
            return false;
        }

    };
    public static Enchantment glow;

    public static void init(){
        glow = Glow.init();
    }//(NOTE|WARN|GOOD|NOPERM|UDAGE|NOTPLAYER)
}