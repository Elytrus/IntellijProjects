package me.tlwv2.bosses.bosses;

import me.tlwv2.bosses.Boss;
import me.tlwv2.bosses.EListener;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.core.utils.LocUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

/**
 * Created by Moses on 2017-07-24.
 */
public class LeMaxwell extends Boss {
    public LeMaxwell(){
        super(4, 32, "\u00a70Maxwell", false, false, true);
    }

    @Override
    protected ItemStack getSpawnItemINIT() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemUtil.addMetadata(item, "\u00a74???!", true, "What is eternal is eternal");

        return item;
    }

    @Override
    protected List<ItemStack> getDropItems() {
        ItemStack drop = new ItemStack(Material.DIAMOND_SWORD);
        drop.setDurability((short)32767);
        drop.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 32767);
        ItemUtil.addMetadata(drop, "\u00a75The Eliminator", false);

        return Collections.singletonList(drop);
    }

    @Override
    protected Creature spawnAnim(Location l) {
        Zombie ent = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        setHP(ent, 1250.0);
        ent.setBaby(false);
        ent.setCanPickupItems(false);
        ent.getEquipment().setBoots(
                ItemUtil.addEnchChainable(new ItemStack(Material.CHAINMAIL_BOOTS),
                new Enchantment[]{
                        Enchantment.DURABILITY,
                        Enchantment.PROTECTION_EXPLOSIONS,
                        Enchantment.PROTECTION_FIRE
                },
                new int[]{
                        32767, 32767, 32767
                }
        ));
        ent.getEquipment().setHelmet(ItemUtil.skull("mishovy"));

        this.spawnCompleted = true;

        return ent;
    }

    @Override
    protected void attack(Creature self, Player target2) {
        Vector vector = new LocUtil().getVel(target2.getLocation().clone().subtract(self.getLocation()));
        randVector(vector, 0.75);
        TNTPrimed tnt = (TNTPrimed) self.getWorld().spawnEntity(self.getLocation(), EntityType.PRIMED_TNT);
        tnt.setCustomName(EListener.NO_EXPLODE_CUSTOMNAME);
        tnt.setFuseTicks(25);
        tnt.setVelocity(vector.multiply(7));
    }

    @Override
    protected void deathAnim(Location l) {

    }

    @Override
    public Boss getNewInstance() {
        return new LeMaxwell();
    }

    private Vector randVector(Vector v, double var){
        return v.add(new Vector(genVar(var), genVar(var), genVar(var)));
    }

    private double genVar(double var){
        return (Math.random() - 0.5) * var;
    }
}
