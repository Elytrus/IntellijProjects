package me.tlwv2.bosses.bosses;

import me.tlwv2.bosses.Boss;
import me.tlwv2.bosses.OwnedBoss;
import me.tlwv2.bosses.abilities.AutoDeath;
import me.tlwv2.core.projectile.particlepatterns.ParticleLineUnstable;
import me.tlwv2.core.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses on 2017-10-05.
 */
public class Minion extends OwnedBoss{
    public Minion() {
        super(20, 16, "\u00a70-playername's minion", true, false);
        abilities.add(new AutoDeath(340));
    }

    @Override
    protected ItemStack getSpawnItemINIT() {
        ItemStack item = new ItemStack(Material.CLAY_BALL, 1);
        item = ItemUtil.addMetadata(item, "\u00a77Spawn Minion", true);

        return item;
    }

    @Override
    protected List<ItemStack> getDropItems() {
        return new ArrayList<>();
    }

    @Override
    protected Creature spawnAnim(Location l) {
        Zombie original = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        setHP(original, 26);
        setRange(original);

        original.setCustomNameVisible(true);
        original.setBaby(false);

        original.getEquipment().setHelmet(ItemUtil.addMetadata(new ItemStack(Material.CHAINMAIL_HELMET), "", true));
        original.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false));

        this.hpBar.setVisible(false);

        this.spawnCompleted = true;

        return original;
    }

    @Override
    protected void attack(Creature self, Player target2) {
        if(target2 == null)
            return;

        Location loc = target2.getLocation();
        Location sLoc = self.getLocation();

        if(sLoc.distance(loc) > 2)
            return;

        target2.damage(2.0);
        target2.setVelocity(target2.getVelocity().clone().add(sLoc.getDirection().clone().multiply(0.5)));
        loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc.clone().add(0, 1, 0), 20, 0.5, 1, 0.5, 0);
    }

    @Override
    protected void deathAnim(Location l) {
        new ParticleLineUnstable(l, l.clone().add(0, 5, 0), 40, 15, Particle.CRIT, 0.2, 0.1, 0.2) {
            @Override
            public void doAfter(Location end) {
                end.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, end, 1);
            }
        }.execute();
    }

    @Override
    public Boss getNewInstance() {
        return new Minion();
    }
}
