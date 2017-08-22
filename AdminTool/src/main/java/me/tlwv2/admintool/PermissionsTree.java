package me.tlwv2.admintool;

import java.util.ArrayList;

/**
 * Created by Moses on 2017-08-21.
 */
public class PermissionsTree {
    private ArrayList<PermissionsTree> trees;

    public String getName() {
        return name;
    }

    private String name;

    public PermissionsTree(String name) {
        this.name = name;
    }

    public void append(String[] nodes){

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PermissionsTree))
            return false;
        return ((PermissionsTree)obj).getName().equals(this.name);
    }

    public static PermissionsTree build(String[] perms){

    }
}
