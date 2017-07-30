package me.tlwv2.core.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class MessageQueue{
    private List<String> lines;
    private CommandSender p;
    private String pfx;

    public MessageQueue(CommandSender p, String pfx){
        lines = new ArrayList<String>();
        this.p = p;
        this.pfx = pfx;
    }

    public void append(String line){
        lines.add(pfx + line);
    }

    public void executeAll(){
        for(String line : lines)
            p.sendMessage(line);
    }

    public void flush(){
        lines.clear();
    }
}