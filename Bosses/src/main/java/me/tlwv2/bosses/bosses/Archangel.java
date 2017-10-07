package me.tlwv2.bosses.bosses;

import me.tlwv2.bosses.Boss;
import me.tlwv2.bosses.abilities.Necromancy;
import me.tlwv2.bosses.abilities.Shock;
import me.tlwv2.core.projectile.particlepatterns.ParticleLine;
import me.tlwv2.bosses.projectiles.Beam;
import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Archangel extends Boss {

    public Archangel() {
        super(15, 40, "§0§lTHE ARCHANGEL", false);
        abilities.add(new Shock());
        abilities.add(new Necromancy());
    }

    @Override
    protected ItemStack getSpawnItemINIT() {
        ItemStack item = new ItemStack(Material.BONE);
        ItemUtil.addMetadata(item, "§0Call of the Archangel", true, "Critical info missing");
        return item;
    }

    @Override
    protected List<ItemStack> getDropItems() {
        int num = random(30, 64);
        ItemStack drops = new ItemStack(Material.GOLDEN_APPLE, num);
        drops.setDurability((short)1);
        return Collections.singletonList(drops);
    }

    @Override
    protected Creature spawnAnim(Location l) {
        Creature ent = (Creature) l.getWorld().spawnEntity(l, EntityType.WITHER_SKELETON);
        setHP(ent, 1250);
        setRange(ent);
        ent.setCanPickupItems(false);
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 32767);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 32767);
        ItemStack chest = new ItemStack(Material.ELYTRA);
        chest.addEnchantment(Enchantment.DURABILITY, 3);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1024);

        ent.getEquipment().setHelmet(helmet);
        ent.getEquipment().setChestplate(chest);
        ent.getEquipment().setBoots(boots);

        this.spawnCompleted = true;

        return ent;
    }

    @Override
    public void deathAnim(Location l) {
        new ParticleLine(l.clone().add(0, 10, 0), l, 100, 20, Particle.PORTAL, null) {

            @Override
            public void doAfter(Location end) {
                end.getWorld().createExplosion(l, 6.0f);
            }
        }.execute();
    }

    @Override
    protected void attack(Creature self, Player target) {
        new Beam(self).launch();
    }

    @Override
    public Boss getNewInstance() {
        // TODO Auto-generated method stub
        return new Archangel();
    }

}
