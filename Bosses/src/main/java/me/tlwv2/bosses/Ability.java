package me.tlwv2.bosses;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

public abstract class Ability {
    protected boolean singleUse;

    public Ability(boolean singleUse){
        this.singleUse = singleUse;
    }

    public abstract boolean condition(Creature self, Player target);
    public abstract void execute(Creature self, Player target);

    public boolean isSingleUse(){
        return singleUse;
    }

    protected static boolean chance(double chance){
        return Math.random() < chance;
    }

    /*public static Ability generateRandomChanceAbility(double chance, BiConsumer<Creature, Player> executeFunc){
        return new Ability(false) {

            @Override
            public void execute(Creature self, Player target) {
                executeFunc.accept(self, target);
            }

            @Override
            public boolean condition(Creature self, Player target) {
                return chance(chance);
            }
        };
    }*/
}
