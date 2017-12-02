package me.tlwv2.bosses.abilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import me.tlwv2.bosses.Ability;
import me.tlwv2.core.utils.LocUtil;

public class Necromancy extends Ability {

    public Necromancy() {
        super(false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean condition(Creature self, Player target) {
        return chance(1.0 / 30.0);
    }

    @Override
    public void execute(Creature self, Player target) {
        //Bukkit.getLogger().info("executing");
        Location l = self.getLocation();
        int numberOfSpawns = 1 + new Random().nextInt(2);
        List<Block> possibilities = Arrays
                .stream(new LocUtil()
                        .draw(l.clone().add(-5, 0, -5), l.clone().add(5, 0, 5)))
                .filter(e -> !e.getType().isSolid())
                .collect(Collectors.toList());
        for(int i = 0; i < numberOfSpawns; i++){
            Block b = getRandomAndPop(possibilities);
            Skeleton s = (Skeleton) b.getWorld().spawnEntity(l, EntityType.SKELETON);
            s.setCustomName("\u00a70Spawn of the Archangel");
            s.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
            s.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
            s.getWorld().spawnParticle(Particle.LAVA, s.getLocation(), 50, 0.5, 0.25, 0.5, 0);
        }
    }

    private <T> T getRandomAndPop(List<T> l){
        int index = new Random().nextInt(l.size());
        T val = l.get(index);
        l.remove(index);
        return val;
    }
}
