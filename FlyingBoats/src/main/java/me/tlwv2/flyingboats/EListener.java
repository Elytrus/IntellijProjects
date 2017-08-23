package me.tlwv2.flyingboats;

import me.tlwv2.core.utils.LocUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

/**
 * Created by Moses on 2017-08-17.
 */
public class EListener implements Listener{
    public static final double MULTIPLIER = 8;
    public static final double ACCEL_CONSTANT = 0.1;
    public static final double Y_ACCEL_CONSTANT = 0.1;
    public static final double DOWN_ACCEL_CONSTANT = 0.05;

    public EListener(FlyingBoats plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(FlyingBoats.self.isInFlyingBoat(p)){
            String msg = FlyingBoats.self.toggleStopped(FlyingBoats.self.getFlyingBoat(p)) == 1 ? "Started" : "Stopped";
            p.sendTitle(ChatColor.RED + "Engine " + msg, "", 10, 0, 10);
        }
    }

    @EventHandler
    public void onBoatMove(VehicleMoveEvent e){
        Vehicle vehicle = e.getVehicle();

        if(FlyingBoats.self.isFlyingBoat(vehicle) && !vehicle.getPassengers().isEmpty()){
            Boat b = (Boat) vehicle;
            Vector d = new LocUtil().getVel(b.getPassengers().get(0).getLocation());
            Vector maxDelta = d.clone().multiply(MULTIPLIER);
            Vector accelDelta = d.clone().multiply(ACCEL_CONSTANT);

            b.getWorld().spawnParticle(Particle.CRIT, b.getLocation().subtract(d), 20, 0.1, 0.1, 0.1);

            Vector v = b.getVelocity();

            b.getPassengers().forEach(ee -> ee.sendMessage(String.format("Before: %.3f %.3f %.3f", v.getX(), v.getY(), v.getZ())));

            v.setX(headToward(v.getX(), maxDelta.getX(), accelDelta.getX()));
            v.setZ(headToward(v.getZ(), maxDelta.getZ(), accelDelta.getZ()));

            b.getPassengers().forEach(ee -> ee.sendMessage(String.format("After: %.3f %.3f %.3f", v.getX(), v.getY(), v.getZ())));

            if(!FlyingBoats.self.isStopped(b))
                v.setY(headToward(v.getY(), 2, Y_ACCEL_CONSTANT));
            else
                v.setY(headToward(v.getY(), -1.3, DOWN_ACCEL_CONSTANT));

            b.getPassengers().get(0).sendMessage(String.format("Max velocity: %.3f %.3f %.3f", maxDelta.getX(), maxDelta.getY(), maxDelta.getZ()));

            b.setVelocity(v);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        Entity ee = e.getEntity();

        if(FlyingBoats.self.isFlyingBoat(ee))
            FlyingBoats.self.remove(ee);
    }

    @EventHandler
    public void onEnterVehicle(VehicleEnterEvent e){
        Entity ee = e.getEntered();
        if(ee instanceof Player){
            Player p = (Player) ee;
            p.setAllowFlight(true);
            p.setFlying(true);
        }
    }

    @EventHandler
    public void onExitVehicle(VehicleExitEvent e){
        Entity ee = e.getExited();
        if(ee instanceof Player){
            Player p = (Player) ee;
            p.setFlying(false);
            p.setAllowFlight(false);
        }
    }

    private double headToward(double original, double border, double accelConstant){
        //double delta = Math.abs(original * accelConstant);
        //System.out.println("delta: " + delta);

        if(original < border){
            double n = original + accelConstant;//original + delta;
            return n <= border ? n : border;
        }
        double n = original - accelConstant;//original - delta;
        return n >= border ? n : border;
    }

    private void headToward(Vector original, Vector border, Vector accelVector){
        original.setX(headToward(original.getX(), border.getX(), accelVector.getX()));
        original.setY(headToward(original.getY(), border.getY(), accelVector.getY()));
        original.setZ(headToward(original.getZ(), border.getZ(), accelVector.getZ()));
    }
}
