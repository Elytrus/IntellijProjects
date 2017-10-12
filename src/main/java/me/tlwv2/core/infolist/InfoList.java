package me.tlwv2.core.wrappers.infolist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InfoList {
    HashMap<String, String> permissionmap;

    public InfoList(){
        permissionmap = new HashMap<String, String>();
    }

    public void add(String perm, String desc){
        permissionmap.put(perm, desc);
    }

    public boolean exists(String perm){
        return permissionmap.containsKey(perm);
    }

    public String dump(String delimeter){
        return permissionmap.keySet().stream()
                .collect(Collectors.joining(delimeter));
    }

    public String getDesc(String perm){
        return permissionmap.get(perm);
    }

    public List<String> string(String format){
        ArrayList<String> arr = new ArrayList<String>();
        permissionmap.forEach((k, v) -> arr.add(String.format(format, k, v) + "\n"));
        return arr;
    }
}
