package me.tlwv2.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.UUID;

/**
 * Created by Moses on 2017-10-06.
 */
public abstract class OwnedBoss extends Boss{
    protected UUID ownerUUID;
    protected String ownerName;

    public OwnedBoss(int attackDelay, double aggroRange, String name, boolean allowNullTarget, boolean showHealthBar) {
        super(attackDelay, aggroRange, name, true, allowNullTarget, showHealthBar);
    }

    @Override
    public void spawn(Location l, Player owner) {
        this.ownerUUID = owner.getUniqueId();
        this.ownerName = owner.getName();

        this.name = this.name.replaceAll("-playername", this.ownerName);
        this.hpBar.setTitle(name);
        super.spawn(l, owner);
    }

    @Override
    public Player target(Location here){
        return here.getWorld().getNearbyEntities(here, aggroRange, aggroRange, aggroRange)
                .stream()
                .filter(e -> e instanceof Player)
                .filter(e -> !e.getUniqueId().equals(this.ownerUUID))
                .map(Player.class::cast)
                .sorted(Comparator.comparingDouble(a -> a.getLocation().distance(here)))
                .findFirst()
                .orElse(null);
    }
}
