package me.tlwv2.passiveaddons.enchantments.debuff.effect;

import me.tlwv2.passiveaddons.enchantments.BaseCustomEnchantment;

public class Poison extends BaseCustomEnchantment {
    public Poison(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "Toxic";
    }
}
