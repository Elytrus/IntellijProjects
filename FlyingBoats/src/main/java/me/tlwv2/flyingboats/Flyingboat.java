package me.tlwv2.flyingboats;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * Created by Moses on 2017-08-21.
 */
public class Flyingboat {
    public Flyingboat() {

    }

    public void onMove(PlayerMoveEvent e){
        Boat b = (Boat) vehicle;
        b.getWorld().spawnParticle(Particle.CRIT, b.getLocation(), 20, 0.5, 0.2, 0.5);
        //b.getPassengers().forEach(ee -> ee.sendMessage("dingus dongus"));

        Vector v = b.getVelocity();
        Vector d = vehicle.getVelocity();//new LocUtil().getVel(b.getLocation());

        //b.getPassengers().forEach(ee -> ee.sendMessage(v.getX() + " " + v.getY() + " " + v.getZ()));
        //b.getPassengers().forEach(ee -> ee.sendMessage(v.getX() + " a " + v.getY() + " a " + v.getZ()));

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
                headToward(v, new Vector(0, -3, 0), ACCEL_CONSTANT);
            }
            else if(type == Material.GLOWSTONE_DUST){
                v.setY(headToward(v.getY(), 0, ACCEL_CONSTANT));
            }
            else{
                headToward(v, new Vector(MOVEMENT_MAX, MOVEMENT_MAX, MOVEMENT_MAX), ACCEL_CONSTANT);
            }
        }

        b.setVelocity(v);
    }
}
