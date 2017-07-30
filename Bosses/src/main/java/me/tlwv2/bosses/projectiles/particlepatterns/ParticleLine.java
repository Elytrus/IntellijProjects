package me.tlwv2.bosses.projectiles.particlepatterns;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.tlwv2.bosses.Bosses;

public abstract class ParticleLine {
    private Location loc;
    private Vector speed;
    private int timeInTicks;
    private int timeLeft;
    private Particle particle;
    private Object arg;
    private int particleCount;

    public ParticleLine(Location loc, Location end, int timeInTicks, int particleCount, Particle particle, Object arg){
        this.loc = loc;
        this.speed = end.clone().subtract(loc).multiply(1.0 / (double)timeInTicks).toVector();
        //Bukkit.getLogger().info("" + end.clone().subtract(loc));
        this.timeInTicks = timeInTicks;
        this.timeLeft = this.timeInTicks;
        this.particle = particle;
        this.arg = arg;
        this.particleCount = particleCount;
    }

    public void execute(){
        new BukkitRunnable(){

            @Override
            public void run() {
                loc.getWorld().spawnParticle(particle, loc, particleCount, 0, 0, 0, arg);
                loc.add(speed);
                timeLeft--;
                if(timeLeft <= 0){
                    doAfter(loc);
                    this.cancel();
                }
            }
        }.runTaskTimer(Bosses.self, 0, 1);
    }

    public abstract void doAfter(Location end);
}
