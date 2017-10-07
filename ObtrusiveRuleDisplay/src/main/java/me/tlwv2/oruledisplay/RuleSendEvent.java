package me.tlwv2.oruledisplay;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Moses on 2017-09-09.
 */
public class RuleSendEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    Player player;

    public Player getPlayer() {
        return player;
    }

    public RuleSendEvent(Player player){
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
