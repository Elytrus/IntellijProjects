package me.tlwv2.kitsp.folders;

import me.tlwv2.kitsp.Kit;
import me.tlwv2.kitsp.Main;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class KitSelectorSetup implements Listener {
    private Main main;
    private Kit[][] kitPages;
    private int kitCount;
    private int currentPage;
    private boolean complete;

    public KitSelectorSetup(){
        this.main = Main.instance();
        ArrayList<Kit> kits = new ArrayList<>(main.getKits().values());
        int pageCount = kits.size() / 45 + 1;

        this.kitPages = new Kit[pageCount][45];
        for(int i = 0; i < pageCount; i++){
            int offset = 45 * i;
            for(int j = 0; j < 45; j++){
                kitPages[i][j] = kits.get(offset + j);
            }
        }

        this.kitCount = kits.size();
        this.complete = false;
        this.currentPage = -1;
    }

    public Inventory createInventoryFor
}
