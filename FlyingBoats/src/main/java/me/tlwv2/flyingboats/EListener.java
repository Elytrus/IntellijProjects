package me.tlwv2.flyingboats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    //public static final double MULTIPLIER = 2;
    public static final double ACCEL_CONSTANT = 0.1;
    public static final double MOVEMENT_MAX = 2.0;

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

    private void headToward(Vector original, Vector border, double accelConstant){
        original.setX(headToward(original.getX(), border.getX(), accelConstant));
        original.setY(headToward(original.getY(), border.getY(), accelConstant));
        original.setZ(headToward(original.getZ(), border.getZ(), accelConstant));
    }
}
