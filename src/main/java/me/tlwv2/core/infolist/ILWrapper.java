package me.tlwv2.core.infolist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ILWrapper {
    static Map<String, InfoList> cmds = new HashMap<String, InfoList>();
    static Map<String, InfoList> perms = new HashMap<String, InfoList>();

    static final String CMDFORMAT = "§e/%s: %s";
    static final String PERMFORMAT = "§e%s: %s";

    static void add(String a, String b, Plugin p, boolean perm){
        if(perm)
            perms.get(p.getName()).add(a, b);
        else
            cmds.get(p.getName()).add(a, b);
    }

    static void addAll(String[] ss, Plugin p, boolean perm){
        if(ss.length % 2 != 0){
            Bukkit.getLogger().severe("Critical error occured in ILWrapper class, pls fix now or maxwell");
            return;
        }

        for(int i = 0; i < ss.length; i+= 2)
            add(ss[i], ss[i + 1], p, perm);
    }

    public static void addPerm(String a, String b, Plugin p){
        add(a, b, p, true);
    }

    public static void addCmd(String a, String b, Plugin p){
        add(a, b, p, false);
    }

    public static void addAllPerm(String[] ss, Plugin p){
        addAll(ss, p, true);
    }

    public static void addAllCmd(String[] ss, Plugin p){
        addAll(ss, p, false);
    }

    public static void registerPlugin(Plugin p){
        cmds.put(p.getName(), new InfoList());
        perms.put(p.getName(), new InfoList());
    }

    public static void dump(CommandSender p, String pp){
        p.sendMessage("§e-[Commands]-");
        p.sendMessage(cmds.get(pp).string(CMDFORMAT).toArray(new String[0]));
        p.sendMessage("§e-[Permissions]-");
        p.sendMessage(perms.get(pp).string(PERMFORMAT).toArray(new String[0]));
    }

    public static void dumpAll(CommandSender p){
        p.sendMessage("§e-[Commands]-");
        for(Entry<String, InfoList> pcmds : cmds.entrySet()){
            p.sendMessage("§e--[Plugin " + pcmds.getKey() + "]--");
            p.sendMessage(pcmds.getValue().string(CMDFORMAT).toArray(new String[0]));
        }
        p.sendMessage("§e-[Permissions]-");
        for(Entry<String, InfoList> pcmds : perms.entrySet()){
            p.sendMessage("§e--[Plugin " + pcmds.getKey() + "]--");
            p.sendMessage(pcmds.getValue().string(PERMFORMAT).toArray(new String[0]));
        }
    }

    public static Collection<String> getRegisteredPlugins(){
        return cmds.keySet();
    }

    public static boolean isRegisteredPlugin(String s){
        return getRegisteredPlugins().contains(s);
    }
}
