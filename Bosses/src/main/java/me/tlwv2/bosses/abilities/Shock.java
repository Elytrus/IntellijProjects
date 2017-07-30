package me.tlwv2.bosses.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.tlwv2.bosses.Ability;

public class Shock extends Ability {

    public Shock() {
        super(true);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean condition(Creature self, Player target) {
        return self.getLocation().distance(target.getLocation()) < 5;
    }

    @Override
    public void execute(Creature self, Player target) {
        Location l = self.getLocation();
        l.getWorld().spawnParticle(Particle.CRIT_MAGIC, l, 50, 2, 1.5, 2);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
    }
}
