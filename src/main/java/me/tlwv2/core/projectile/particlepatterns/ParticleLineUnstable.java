package me.tlwv2.core.projectile.particlepatterns;

import org.bukkit.Location;
import org.bukkit.Particle;

public abstract class ParticleLineUnstable extends ParticleLine{
    public ParticleLineUnstable(Location loc, Location end, int timeInTicks, int particleCount, Particle particle, double offsetX, double offsetY, double offsetZ,
                                Object arg){
        super(loc, end, timeInTicks, particleCount, particle, arg);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public ParticleLineUnstable(Location loc, Location end, int timeInTicks, int particleCount, Particle particle, double offsetX, double offsetY, double offsetZ){
        this(loc, end, timeInTicks, particleCount, particle, offsetX, offsetY, offsetZ, null);
    }
}
