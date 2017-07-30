package me.tlwv2.bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Boss{
    protected ItemStack spawn;
    protected Creature bossEntity;
    protected Player target;
    protected BossBar hpBar;
    protected BukkitRunnable tickActionExecutor;
    protected boolean spawnCompleted;

    protected CopyOnWriteArrayList<Ability> abilities;
    protected int attackDelay;
    protected int currAttackDelay;
    protected double aggroRange;

    protected String name;

    public Boss(int attackDelay, double aggroRange, String name){
        this.spawn = getSpawnItemINIT();
        this.bossEntity = null;
        this.target = null;
        this.hpBar = Bukkit.createBossBar(name, BarColor.RED, BarStyle.SOLID);
        this.tickActionExecutor = new BukkitRunnable() {

            @Override
            public void run() {
                preformTickActions();
            }
        };
        this.spawnCompleted = false;

        this.abilities = new CopyOnWriteArrayList<Ability>();
        this.attackDelay = attackDelay;
        this.currAttackDelay = this.attackDelay;
        this.aggroRange = aggroRange;

        this.name = name;
    }

    protected abstract ItemStack getSpawnItemINIT();
    protected abstract List<ItemStack> getDropItems();
    protected abstract Creature spawnAnim(Location l);
    protected abstract void attack(Creature self, Player target2);
    protected abstract void deathAnim(Location l);
    public abstract Boss getNewInstance();

    protected static void setHP(Creature ent, double hp){
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
    }

    protected void setRange(Creature ent){
        ent.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(this.aggroRange);
    }

    protected static int random(int bottom, int top){
        return new Random().nextInt(top - bottom) + bottom;
    }

    //Overrideable
    public Player target(Location here){
        return here.getWorld().getNearbyEntities(here, aggroRange, aggroRange, aggroRange)
                .stream()
                .filter(e -> e instanceof Player)
                .map(Player.class::cast)
                .sorted((a, b) -> Double.compare(a.getLocation().distance(here), b.getLocation().distance(here)))
                .findFirst()
                .orElse(null);
    }

    public ItemStack getSpawnItem(){
        return spawn;
    }

    public boolean isDead(){
        return null == bossEntity;
    }

    public void preformTickActions(){
        this.target = this.target(bossEntity.getLocation());
        currAttackDelay = currAttackDelay > 0 ? currAttackDelay - 1 : attackDelay;

        this.bossEntity.getWorld().getNearbyEntities(this.bossEntity.getLocation(), 50, 50, 50)
                .stream()
                .filter(e -> e instanceof Player)
                .map(Player.class::cast)
                .filter(e -> !hpBar.getPlayers().contains(e))
                .forEach(hpBar::addPlayer);
        hpBar.setProgress((float)(this.bossEntity.getHealth() /
                this.bossEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));

        if(null == this.target)
            return;
        bossEntity.setTarget(target);
        this.abilities.forEach(e -> {
            if(e.condition(bossEntity, target)){
                e.execute(bossEntity, target);
                if(e.isSingleUse())
                    abilities.remove(e);
            }
        });
        if(currAttackDelay == 0)
            this.attack(bossEntity, target);
    }

    public void spawn(Location l){
        this.bossEntity = this.spawnAnim(l);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(spawnCompleted){
                    target(l);
                    tickActionExecutor.runTaskTimer(Bosses.self, 0, 1);
                    //COMMON TO ALL BOSSES
                    bossEntity.setHealth(bossEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    bossEntity.setCustomName(name);
                    bossEntity.setCustomNameVisible(true);
                    this.cancel();
                }
            }
        }.runTaskTimer(Bosses.self, 0L, 1L);
    }

    public void death(Location l){
        this.tickActionExecutor.cancel();
        this.deathAnim(l);
        this.bossEntity = null;
        this.target = null;
        this.getDropItems().stream().forEach(e -> l.getWorld().dropItemNaturally(l, e));
        this.hpBar.removeAll();
        Bosses.self.removeBoss(this);
    }

    public Creature getEntity(){
        return bossEntity;
    }

    @Override
    public String toString(){
        return name;
    }
}
