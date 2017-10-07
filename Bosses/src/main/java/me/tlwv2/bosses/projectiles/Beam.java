package me.tlwv2.bosses.projectiles;

import me.tlwv2.bosses.Bosses;
import me.tlwv2.core.projectile.Projectile;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Beam extends Projectile {

    public Beam(LivingEntity le) {
        super(le, 0.75, 200);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onTick(Location l) {
        l.getWorld().spawnParticle(Particle.REDSTONE, l, 0, 0.5, 0, 0.5, 1);
    }

    @Override
    public void onCollision(Location l) {
        FireworkEffect fwe = FireworkEffect.builder()
                .withColor(Color.PURPLE)
                .withFade(Color.BLACK)
                .flicker(true)
                .build();
        Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(fwe);
        fw.setFireworkMeta(fwm);
        fw.playEffect(EntityEffect.FIREWORK_EXPLODE);
        new BukkitRunnable(){

            @Override
            public void run() {
                fw.remove();
            }

        }.runTaskLater(Bosses.self, 2);
        l.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5).stream()
                .filter(e -> e instanceof LivingEntity)
                .filter(e -> !e.equals(launcher))
                .map(LivingEntity.class::cast)
                .filter(this::isSolid)
                .forEach(e -> {
                    e.damage(6);
                    e.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 160, 3));
                });
    }

    @Override
    public boolean isSolid(LivingEntity le) {
        if(null == le.getCustomName())
            return true;
        return !le.getCustomName().equals("§0Spawn of the Archangel");
    }
}
