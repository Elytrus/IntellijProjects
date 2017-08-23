package me.tlwv2.admintool;

import java.util.ArrayList;

/**
 * Created by Moses on 2017-08-21.
 */
public class PermissionsTree {
    private ArrayList<PermissionsTree> children;

    public String getName() {
        return name;
    }

    private String name;

    public PermissionsTree(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void append(String[] node){
        String name = node[0];

        if(children.contains(new PermissionsTree(name))){
            PermissionsTree p;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PermissionsTree))
            return false;
        return ((PermissionsTree)obj).getName().equals(this.name);
    }

    public static PermissionsTree build(String[] perms){
        return null;
    }
}
