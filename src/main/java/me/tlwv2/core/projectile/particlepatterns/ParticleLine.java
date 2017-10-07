package me.tlwv2.core.projectile.particlepatterns;

import me.tlwv2.core.Core;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public abstract class ParticleLine {
    protected Location loc;
    protected Vector speed;
    protected int timeInTicks;
    protected int timeLeft;
    protected Particle particle;
    protected Object arg;
    protected int particleCount;

    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;

    public ParticleLine(Location loc, Location end, int timeInTicks, int particleCount, Particle particle, Object arg){
        this.loc = loc;
        this.speed = end.clone().subtract(loc).multiply(1.0 / (double)timeInTicks).toVector();
        //Bukkit.getLogger().info("" + end.clone().subtract(loc));
        this.timeInTicks = timeInTicks;
        this.timeLeft = this.timeInTicks;
        this.particle = particle;
        this.arg = arg;
        this.particleCount = particleCount;

        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
    }

    public void execute(){
        new BukkitRunnable(){

            @Override
            public void run() {
                loc.getWorld().spawnParticle(particle, loc, particleCount, offsetX, offsetY, offsetZ, arg);
                loc.add(speed);
                timeLeft--;
                if(timeLeft <= 0){
                    doAfter(loc);
                    this.cancel();
                }
            }
        }.runTaskTimer(Core.self, 0, 1);
    }

    public abstract void doAfter(Location end);
}
