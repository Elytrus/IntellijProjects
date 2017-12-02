package me.tlwv2.survivaladdon.addons;

import me.tlwv2.survivaladdon.Addon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Moses on 2017-11-23.
 */
public class TimedAddon extends Addon{
    private BukkitRunnable clock;
    private int period;

    public TimedAddon(JavaPlugin plugin, double pointCount, double multiplier, int period, Player host) {
        super(plugin, pointCount, multiplier, host);
        this.period = period;

        this.clock = new BukkitRunnable() {
            @Override
            public void run() {
                reward();
            }
        };
        this.clock.runTaskTimer(plugin, 0, period);
    }

    @Override
    public void die() {
        clock.cancel();
    }
}
