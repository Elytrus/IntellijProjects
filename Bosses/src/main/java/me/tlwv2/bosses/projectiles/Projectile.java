package me.tlwv2.bosses.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.tlwv2.bosses.Bosses;
import me.tlwv2.core.utils.LocUtil;

public abstract class Projectile {
    private double speedPerTick;
    private Vector direction;
    private Location location;
    private int distLeft;
    protected LivingEntity launcher;

    public abstract void onTick(Location l);
    public abstract void onCollision(Location l);
    public abstract boolean isSolid(LivingEntity le);

    public Projectile(LivingEntity le, double speedPerTick, int maxDist){
        this.launcher = le;
        this.speedPerTick = speedPerTick;
        this.direction = new LocUtil().getVel(le);
        this.location = le.getLocation().clone().add(0, 1.5, 0);
        this.distLeft = maxDist;
    }

    public boolean collision(Location l){
        return l.getBlock().getType().isSolid() ||
                l.getWorld().getNearbyEntities(l, 0.3, 0.3, 0.3).stream()
                        .filter(e -> e instanceof LivingEntity)
                        .map(LivingEntity.class::cast)
                        .filter(e -> !e.equals(launcher))
                        .filter(this::isSolid)
                        .count() > 0;
    }

    public void launch(){
        new BukkitRunnable() {

            @Override
            public void run() {
                if(distLeft == 0)
                    this.cancel();
                if(collision(location)){
                    onCollision(location);
                    this.cancel();
                }
                onTick(location);
                distLeft--;
                location.add(direction.clone().multiply(speedPerTick));
            }
        }.runTaskTimer(Bosses.self, 0, 1);
    }

	/*private List<Block> getNearbyAndSelf(Location l){
		return Arrays.asList(l.getBlock(),
				l.clone().add(1, 0, 0).getBlock()
				,l.clone().add(-1, 0, 0).getBlock()
				,l.clone().add(0, 1, 0).getBlock()
				,l.clone().add(0, -1, 0).getBlock()
				,l.clone().add(0, 0, 1).getBlock()
				,l.clone().add(0, 0, -1).getBlock());
	}*/
}
