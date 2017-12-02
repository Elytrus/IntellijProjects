package me.tlwv2.bosses.abilities;

import me.tlwv2.bosses.Ability;
import me.tlwv2.bosses.Boss;
import me.tlwv2.bosses.Bosses;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

/**
 * Created by Moses on 2017-10-07.
 */
public class AutoDeath extends Ability {
    private int waitInTicks;

    public AutoDeath(int waitInTicks) {
        super(true);
        this.waitInTicks = waitInTicks;
    }

    @Override
    public boolean condition(Creature self, Player target) {
        return true;
    }

    @Override
    public void execute(Creature self, Player target) {
//        Bukkit.getLogger().info("started autodeath at " + waitInTicks / 20 + " seconds");
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bosses.self, () -> {
//            Bukkit.getLogger().info("DEATH IS HERE");
            Boss.killEntity(self);
        }, this.waitInTicks);
    }
}
