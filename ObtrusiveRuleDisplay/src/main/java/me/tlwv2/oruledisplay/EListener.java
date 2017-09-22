package me.tlwv2.oruledisplay;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EListener implements Listener {
    public EListener(ObtrusiveRuleDisplay obtrusiveRuleDisplay) {
        Bukkit.getPluginManager().registerEvents(this, obtrusiveRuleDisplay);
    }

    @EventHandler
    public void onRuleRequest(RuleSendEvent e){
        Player p = e.getPlayer();

        //Bukkit.getLogger().info(p.hasPermission(ObtrusiveRuleDisplay.BYPASS_PERM) + "");

        if(p.hasPermission(ObtrusiveRuleDisplay.BYPASS_PERM))
            return;

        List<String> rules = ObtrusiveRuleDisplay.self.getRules();
        String prefix = ObtrusiveRuleDisplay.self.getPrefix();

        int fadeIn = ObtrusiveRuleDisplay.self.getFadeIn();
        int stay = ObtrusiveRuleDisplay.self.getStay();
        int fadeOut = ObtrusiveRuleDisplay.self.getFadeOut();

        int total = fadeIn + stay + fadeOut;
        int size = rules.size();
        MutableInteger index = new MutableInteger();
        index.value = 0;

        //Bukkit.getLogger().info(String.format("Size: %d | Index Val: %d", size, index.value));

        String playerName = p.getName();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(index.value >= size - 1){
                    this.cancel();
                }

                String rule = ChatColor.translateAlternateColorCodes('&', rules.get(index.value));
                String subbedPrefix = ChatColor.translateAlternateColorCodes('&', prefix
                        .replaceAll("\\$number", (index.value + 1) + "")
                        .replaceAll("\\$player", playerName));

                //Bukkit.getLogger().info("sending title num " + index.value);

                p.sendTitle(subbedPrefix, rule, fadeIn, stay, fadeOut);
                index.value++;
            }
        }.runTaskTimer(ObtrusiveRuleDisplay.self, 0, total);
    }
}

class MutableInteger{
    int value;
}