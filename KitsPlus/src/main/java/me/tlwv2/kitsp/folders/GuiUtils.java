package me.tlwv2.kitsp.folders;

import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GuiUtils {
    public static HashMap<Player, PageData> currentMenus;
    public static ItemStack invalidStack, insertFolderStack, backStack, forwardStack, exitStack, insertKitStack;

    static{
        invalidStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15), "\u00a70-", true);
        insertFolderStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7), "\u00a7aInsert Folder", true);
        backStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13), "\u00a7aBack", true);
        forwardStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4), "\u00a7aNext", true);
        exitStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), "\u00a7cExit", true);
        insertKitStack =
                ItemUtil.addMetadata(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7), "\u00a7aInsert Kit", true);
    }
}
