import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class OpItemFix extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        for(ItemStack item : new ItemStack[]{e.getCurrentItem(), e.getCursor()}){
            if(item == null){
                continue;
            }

            boolean pass = true;
            for(int level : item.getEnchantments().values()){
                if(level > 5){
                    pass = false;
                    break;
                }
            }

            if(!pass){
                item.setAmount(0);
                item.setType(Material.AIR);
                Bukkit.broadcastMessage(ChatColor.RED + "One OP item was found! Alert le police!!!");
            }
        }
    }
}
