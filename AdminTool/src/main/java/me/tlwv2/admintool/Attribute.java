package me.tlwv2.admintool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import me.tlwv2.core.utils.ItemData;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagInt;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagString;

public class Attribute{
    private String name;
    private int amount;
    private int operation;
    private int uuidleast;
    private int uuidmost;
    static final String ATTRIBNAME = "AttributeModifiers";

    public Attribute(String attribnm, int amt, int op){
        this.name = attribnm;
        this.amount = amt;
        this.operation = op;

        generateUUID();
    }

    public Attribute(NBTTagCompound attrib){
        this.name = attrib.getString("AttributeName");
        this.amount = attrib.getInt("Amount");
        this.operation = attrib.getInt("Operation");
        this.uuidleast = attrib.getInt("UUIDLeast");
        this.uuidmost = attrib.getInt("UUIDMost");

    }

    public void generateUUID(){
        this.uuidleast = returnRandom(1000, 50000);
        this.uuidmost = returnRandom(50001, 500000);
    }

    private int returnRandom(int floor, int ceiling){
        return new Random().nextInt(ceiling - floor) + floor;
    }

    public String toString(){
        return "§eAttribute Name: " + name + " | Amount: " + amount +
                " | Operation: " + getOp(operation);
    }

    public String getOp(int op){
        return op == 1 ? "1 (+)" : "2 (*)";
    }

    public ItemStack apply(ItemStack i){
        NBTTagList attribs;
        NBTTagCompound nbt = ItemData.getTagCompoundOf(i);

        if(nbt.hasKey(ATTRIBNAME))
            attribs = nbt.getList(ATTRIBNAME, 0);
        else
            attribs = new NBTTagList();

        NBTTagCompound attribute = new NBTTagCompound();
        attribute.set("AttributeName", new NBTTagString(name));
        attribute.set("Name", new NBTTagString(name));
        attribute.set("Amount", new NBTTagInt(amount));
        attribute.set("Operation", new NBTTagInt(operation));
        attribute.set("UUIDLeast", new NBTTagInt(uuidleast));
        attribute.set("UUIDMost", new NBTTagInt(uuidmost));

        attribs.add(attribute);

        nbt.set(ATTRIBNAME, attribs);

        return ItemData.setTagCompoundOf(i, nbt);
    }

    public static List<String> getAttribs(ItemStack it){
        List<String> attribs = new ArrayList<String>();

        NBTTagCompound comp = ItemData.getTagCompoundOf(it);

        if(!comp.hasKey("AttributeModifiers"))
            attribs.add("§eNo attributes");
        else{
            NBTTagList list = comp.getList("AttributeModifiers", 0);
            for(int i = 0; i < list.size(); i++)
                attribs.add(new Attribute(list.get(i)).toString());
        }

        return attribs;
    }
}