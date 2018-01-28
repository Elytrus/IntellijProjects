package me.tlwv2.customitems.items;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2018-01-24.
 */
public interface IAttack {
    void onAttack(EntityDamageByEntityEvent e, Player p, ItemStack i, int slot);
}
