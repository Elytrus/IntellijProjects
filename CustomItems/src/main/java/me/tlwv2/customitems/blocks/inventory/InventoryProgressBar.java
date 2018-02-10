package me.tlwv2.customitems.blocks.inventory;

import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2018-01-25.
 */
public class InventoryProgressBar {
    public static final Material BAR_TYPE = Material.STAINED_GLASS_PANE;

    public int[] slots;
    public int length;
    public short colourBase, colourBar;
    public Inventory inventory;

    public double progress;
    public String textFormat; //Use <p> for progress;

    public InventoryProgressBar(Inventory inventory, String textFormat, int colourBase, int colourBar, int... slots) {
        this.slots = slots;
        this.length = slots.length;
        this.textFormat = textFormat;
        this.colourBase = (short)colourBase;
        this.colourBar = (short)colourBar;
        this.inventory = inventory;

        for(int x : slots){
            inventory.setItem(x, base());
        }
    }

    public void setProgress(double progress){
        int count = (int)(progress * length);

        for(int i = 0; i < count; i++){
            inventory.setItem(slots[i], bar());
        }

        for(int i = count; i < length; i++){
            inventory.setItem(slots[i], base());
        }
    }

    public double getProgress(double progress){
        return progress;
    }

    private String text(){
        return textFormat.replaceAll("<p>", progress + "");
    }

    private ItemStack base(){
        return ItemUtil.addMetadata(new ItemStack(BAR_TYPE, 1, colourBase), text(), true);
    }

    private ItemStack bar(){
        return ItemUtil.addMetadata(new ItemStack(BAR_TYPE, 1, colourBar), text(), true);
    }
}
