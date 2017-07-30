package me.tlwv2.core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class LocUtil {
    public LocUtil(){

    }

    public Block[] draw(Location l1, Location l2){
        return draw(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ(),
                l2.getBlockX(), l2.getBlockY(), l2.getBlockZ(), l1.getWorld());
    }

    public Block[] draw(int x1, int y1, int z1, int x2, int y2, int z2, World w){
        int xdelta = (x2 - x1) /*> 0 ? x2 - x1 : 1*/ + 1;
        int ydelta = (y2 - y1) /*> 0 ? y2 - y1 : 1*/ + 1;
        int zdelta = (z2 - z1) /*> 0 ? z2 - z1 : 1*/ + 1;

        Block[] blocks = new Block[xdelta * ydelta * zdelta];

        int counter = 0;

        for(int x = x1; x <= x2; x++){
            for(int y = y1; y <= y2; y++){
                for(int z = z1; z <= z2; z++){
                    blocks[counter] = w.getBlockAt(x, y, z);
                    counter++;
                }
            }
        }

        return blocks;
    }

    public Vector getVel(LivingEntity le){
        return getVel(le.getLocation());
    }

    public Vector getVel(Location l){
        return getVel(l.getPitch(), l.getYaw());
    }

    public Vector getVel(double pitch, double yaw){
        double p = -pitch;// < 0 ? Math.abs(l.getPitch()) + 180 : l.getPitch();
        double y = yaw + 90;// < 0 ? Math.abs(l.getYaw()) + 180 : l.getYaw();

        return new Vector(Math.cos(Math.toRadians(y)) * (90 - Math.abs(pitch)) / 90,
                Math.sin(Math.toRadians(p))
                ,Math.sin(Math.toRadians(y)) * (90 - Math.abs(pitch)) / 90);
    }

	/*
	public void generateCircle(Particle particle, final double radius, long delay, final double increment, Location center, Plugin p){
		final DDouble angle = new DDouble(1);

		new BukkitRunnable() {

			@Override
			public void run() {
				Vector currentDirectionVector = getVel(0, angle.d()).multiply(radius);

				center.getWorld().spawnParticle(particle, center.add(currentDirectionVector), 1);

				if(angle.d() > 360)
					this.cancel();

				angle.d(angle.d() + increment);
			}
		}.runTaskTimer(p, 0, delay);
	}*/
}

class DDouble{
    public double d;

    public DDouble(double d){
        this.d = d;
    }

    public double d(){return d;}
    public void d(double d){this.d = d;}
}
