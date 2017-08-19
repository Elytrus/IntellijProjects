package me.tlwv2.flyingboats;

import me.tlwv2.core.utils.LocUtil;
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

    public static final double MULTIPLIER = 2;
    public static final double ACCEL_CONSTANT = 0.3;

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
            b.getWorld().spawnParticle(Particle.CRIT, b.getLocation(), 20, 0.5, 0.2, 0.5);
            //b.getPassengers().forEach(ee -> ee.sendMessage("dingus dongus"));

            Vector v = b.getVelocity();
            Vector d = new LocUtil().getVel(b.getLocation());

            headToward(v, d.multiply(MULTIPLIER), ACCEL_CONSTANT);

            if(!FlyingBoats.self.isStopped(b))
                v.setY(headToward(v.getY(), 0.9, ACCEL_CONSTANT));
            else
                v.setY(headToward(v.getY(), -0.25, ACCEL_CONSTANT));

            Entity entity = b.getPassengers().get(0);
            if(entity instanceof Player){
                Player p = (Player) entity;
                Material type = p.getInventory().getItemInMainHand().getType();
                if(type == Material.REDSTONE){
                    v.setX(headToward(v.getX(), 0, ACCEL_CONSTANT));
                    v.setZ(headToward(v.getZ(), 0, ACCEL_CONSTANT));
                }
                else if(type == Material.ANVIL){
                    headToward(v, new Vector(0, -1, 0), ACCEL_CONSTANT);
                }
                else if(type == Material.GLOWSTONE_DUST){
                    v.setY(headToward(v.getY(), 0, ACCEL_CONSTANT));
                }
            }

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
        if(original < border){
            double n = original + accelConstant;
            return n <= border ? n : border;
        }
        double n = original - accelConstant;
        return n >= border ? n : border;
    }

    private void headToward(Vector original, Vector border, double accelConstant){
        original.setX(headToward(original.getX(), border.getX(), accelConstant));
        original.setY(headToward(original.getY(), border.getY(), accelConstant));
        original.setZ(headToward(original.getZ(), border.getZ(), accelConstant));
    }
}
