package me.tlwv2.factionfly.api;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Wrapper for bossbar and player :)
 */
public class PlayerProgressBar{
    private Player player;
    private BossBar bar;
    private int length;
    private int curr;
    private String title;
    private boolean complete;

    public PlayerProgressBar(Player player, String title, BarColor color, int length) {
        this.player = player;
        this.title = title;
        this.length = length;
        this.curr = length;

        this.bar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
        this.bar.addPlayer(player);
        this.complete = false;
    }

    public void tick(){
        this.curr--;
        bar.setProgress((double)curr / (double)length);

        if(curr == 0){
            this.bar.removeAll();
            complete = true;
        }
    }
}
